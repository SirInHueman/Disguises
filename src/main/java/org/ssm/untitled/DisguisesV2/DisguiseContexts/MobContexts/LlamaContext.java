package org.ssm.untitled.DisguisesV2.DisguiseContexts.MobContexts;

import org.bukkit.Sound;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.PitchLockContext;

public class LlamaContext extends PitchLockContext {

    @Override
    public Sound getHurtSound() {
        return Sound.ENTITY_LLAMA_HURT;
    }

    @Override
    public double getWidth() {
        return 0.9;
    }

    @Override
    public double getHeight() {
        return 1.87;
    }
}
