package org.ssm.untitled.DisguisesV2.DisguiseContexts.MobContexts;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Sound;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.FlyingContext;

public class BeeContext extends FlyingContext {

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_BEE_HURT;
    }

    @Override
    public double getWidth() {
        return 0.7;
    }

    @Override
    public double getHeight() {
        return 0.6;
    }

    @Override
    public PacketContainer onMove(PacketContainer playerPacket, PacketContainer mobPacket) {
        if (playerPacket.getType() == PacketType.Play.Server.ENTITY_HEAD_ROTATION){
            return super.onMove(playerPacket, mobPacket);
        }
        mobPacket.getBooleans().write(0, false);
        return super.onMove(playerPacket, mobPacket);
    }

    // Set "On Ground" boolean in Movement packets false, also make it easier for
    // Contexts with only Hurt Sounds
}
