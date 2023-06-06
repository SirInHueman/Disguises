package org.ssm.untitled.DisguisesV2.DisguiseContexts.MobContexts;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Sound;
import org.ssm.untitled.DisguisesV2.DisguiseContext;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.PitchLockContext;

public class GuardianContext extends PitchLockContext {

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_GUARDIAN_HURT;
    }

    @Override
    public double getWidth() {
        return 0.85;
    }

    @Override
    public double getHeight() {
        return 0.85;
    }

}
