package org.ssm.untitled.DisguisesV2.DisguiseContexts.MobContexts;

import org.bukkit.Sound;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.FlyingContext;

public class BatContext extends FlyingContext {

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_BAT_HURT;
    }

    @Override
    public double getWidth() {
        return 0.5;
    }

    @Override
    public double getHeight() {
        return 0.9;
    }

}
