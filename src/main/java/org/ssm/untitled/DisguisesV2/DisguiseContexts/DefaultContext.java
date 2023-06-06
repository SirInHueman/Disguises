package org.ssm.untitled.DisguisesV2.DisguiseContexts;

import org.bukkit.Sound;
import org.ssm.untitled.DisguisesV2.DisguiseContext;

public class DefaultContext extends DisguiseContext {

    private double height = 1, width = 1;
    private Sound sound = Sound.ENTITY_PLAYER_HURT;

    public DefaultContext(Sound sound, double height, double width){
        this.sound = sound;
        this.height = height;
        this.width = width;
    }

    public DefaultContext(){}

    @Override
    public Sound getHurtSound() {
        return sound;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    // Make an on-hit method instead?
}
