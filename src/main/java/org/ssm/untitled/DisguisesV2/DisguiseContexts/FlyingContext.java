package org.ssm.untitled.DisguisesV2.DisguiseContexts;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.ssm.untitled.DisguisesV2.DisguiseContext;

public abstract class FlyingContext extends DisguiseContext {

    private final static double modelRaise = 1.35;

    @Override
    public PacketContainer onMove(PacketContainer playerPacket, PacketContainer mobPacket) {
        PacketType type = playerPacket.getType();
        if (type == PacketType.Play.Server.ENTITY_TELEPORT){
            mobPacket.getDoubles().write(1, (mobPacket.getDoubles().read(1)+modelRaise));
        }
        return super.onMove(playerPacket, mobPacket);
    }

    @Override
    public PacketContainer onSpawn(PacketContainer spawnMob) {
        spawnMob.getDoubles().write(1, spawnMob.getDoubles().read(1)+modelRaise);
        return super.onSpawn(spawnMob);
    }
}
