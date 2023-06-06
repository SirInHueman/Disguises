package org.ssm.untitled.DisguisesV2.DisguiseContexts;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.ssm.untitled.DisguisesV2.DisguiseContext;

public abstract class PitchLockContext extends DisguiseContext {

    @Override
    public PacketContainer onMove(PacketContainer playerPacket, PacketContainer mobPacket) {
        PacketType type = playerPacket.getType();
        if (type == PacketType.Play.Server.ENTITY_LOOK || type == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK || type == PacketType.Play.Server.REL_ENTITY_MOVE || type == PacketType.Play.Server.ENTITY_TELEPORT ){
            mobPacket.getBytes().write(1, (byte) (playerPacket.getBytes().read(1)*0.4));
        }
        return super.onMove(playerPacket, mobPacket);
    }
}
