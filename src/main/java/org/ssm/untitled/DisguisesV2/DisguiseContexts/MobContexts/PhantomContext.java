package org.ssm.untitled.DisguisesV2.DisguiseContexts.MobContexts;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Sound;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.FlyingContext;

public class PhantomContext extends FlyingContext {

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_PHANTOM_HURT;
    }

    @Override
    public double getWidth() {
        return 0.9;
    }

    @Override
    public double getHeight() {
        return 0.5;
    }

    @Override
    public PacketContainer onMove(PacketContainer playerPacket, PacketContainer mobPacket) {
        PacketType type = playerPacket.getType();
        if (type == PacketType.Play.Server.ENTITY_LOOK || type == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK || type == PacketType.Play.Server.REL_ENTITY_MOVE || type == PacketType.Play.Server.ENTITY_TELEPORT ){
            byte invertedPitch = (byte)(playerPacket.getBytes().read(1)*-1);
            mobPacket.getBytes().write(1, invertedPitch);
        }
        return super.onMove(playerPacket, mobPacket);
    }
}
