package me.lukedluz.SpecterBanco.API.SignAPI.Protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerUpdateSign extends AbstractPacket {
	@SuppressWarnings("deprecation")
	public static final PacketType TYPE = PacketType.Play.Server.UPDATE_SIGN;

	public WrapperPlayServerUpdateSign() {
		super(new PacketContainer(TYPE), TYPE);
		this.handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerUpdateSign(PacketContainer packet) {
		super(packet, TYPE);
	}

	public BlockPosition getLocation() {
		return (BlockPosition) this.handle.getBlockPositionModifier().read(0);
	}

	public void setLocation(BlockPosition value) {
		this.handle.getBlockPositionModifier().write(0, value);
	}

	public WrappedChatComponent[] getLines() {
		return (WrappedChatComponent[]) this.handle.getChatComponentArrays().read(0);
	}

	public void setLines(WrappedChatComponent[] value) {
		if (value == null) {
			throw new IllegalArgumentException("value cannot be null!");
		}
		if (value.length != 4) {
			throw new IllegalArgumentException("value must have 4 elements!");
		}
		this.handle.getChatComponentArrays().write(0, value);
	}
}
