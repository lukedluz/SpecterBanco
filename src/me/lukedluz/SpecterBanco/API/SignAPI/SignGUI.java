package me.lukedluz.SpecterBanco.API.SignAPI;

import java.util.Hashtable;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.lukedluz.SpecterBanco.API.SignAPI.Protocol.WrapperPlayClientUpdateSign;
import me.lukedluz.SpecterBanco.API.SignAPI.Protocol.WrapperPlayServerBlockChange;
import me.lukedluz.SpecterBanco.API.SignAPI.Protocol.WrapperPlayServerOpenSignEntity;
import me.lukedluz.SpecterBanco.API.SignAPI.Protocol.WrapperPlayServerUpdateSign;

public class SignGUI extends JavaPlugin {
    private static Hashtable<UUID, BlockPosition> playerBlockPositions;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onEnable() {
        playerBlockPositions = new Hashtable();

        registerSignUpdateListener();
    }

    public void onDisable() {
    }

    public static void openSignEditor(Player player, String[] text) {
        int x = player.getLocation().getBlockX();
        int y = 255;
        int z = player.getLocation().getBlockZ();
        BlockPosition bp = new BlockPosition(x, y, z);

        WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange();
        WrappedBlockData blockData = WrappedBlockData.createData(Material.WALL_SIGN);
        blockChangePacket.setBlockData(blockData);
        blockChangePacket.setLocation(bp);
        blockChangePacket.sendPacket(player);

        WrapperPlayServerUpdateSign updateSignPacket = new WrapperPlayServerUpdateSign();
        updateSignPacket.setLocation(new BlockPosition(x, y, z));
        WrappedChatComponent[] lines = new WrappedChatComponent[4];
        lines[0] = WrappedChatComponent.fromText(text[0]);
        lines[1] = WrappedChatComponent.fromText(text[1]);
        lines[2] = WrappedChatComponent.fromText(text[2]);
        lines[3] = WrappedChatComponent.fromText(text[3]);
        updateSignPacket.setLines(lines);

        updateSignPacket.sendPacket(player);

        WrapperPlayServerOpenSignEntity packet = new WrapperPlayServerOpenSignEntity();
        packet.setLocation(new BlockPosition(x, y, z));
        packet.sendPacket(player);

        playerBlockPositions.put(player.getUniqueId(), bp);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerSignUpdateListener() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        if (playerBlockPositions == null) {
            playerBlockPositions = new Hashtable();
        }
        manager.addPacketListener(new PacketAdapter(this, new PacketType[] { PacketType.Play.Client.UPDATE_SIGN }) {
            public void onPacketReceiving(PacketEvent event) {
                String[] text = new String[4];
                Player player = event.getPlayer();
                WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event.getPacket());
                BlockPosition bp = packet.getLocation();

                BlockPosition playerBlockPos = (BlockPosition) SignGUI.playerBlockPositions.get(player.getUniqueId());
                if (playerBlockPos != null) {
                    if ((bp.getX() == playerBlockPos.getX()) && (bp.getY() == playerBlockPos.getY())
                            && (bp.getZ() == playerBlockPos.getZ())) {
                        for (int i = 0; i < packet.getLines().length; i++) {
                            WrappedChatComponent chat = packet.getLines()[i];
                            String str = StringEscapeUtils.unescapeJavaScript(chat.getJson());
                            str = str.substring(1, str.length() - 1);
                            text[i] = str;
                        }
                        WrapperPlayServerBlockChange blockChangePacket = new WrapperPlayServerBlockChange();
                        WrappedBlockData blockData = WrappedBlockData.createData(Material.AIR);
                        blockChangePacket.setBlockData(blockData);
                        blockChangePacket.setLocation(playerBlockPos);
                        blockChangePacket.sendPacket(player);

                        SignGUI.playerBlockPositions.remove(player.getUniqueId());

                        SignGUIUpdateEvent updateEvent = new SignGUIUpdateEvent(player, text);
                        Bukkit.getServer().getPluginManager().callEvent(updateEvent);
                    }
                }
            }

            public void onPacketSending(PacketEvent event) {
            }
        });
    }
}
