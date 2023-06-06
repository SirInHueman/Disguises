package org.ssm.untitled.DisguisesV2;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.DefaultContext;
import org.ssm.untitled.DisguisesV2.DisguiseContexts.MobContexts.*;

import java.util.Arrays;

public abstract class DisguiseContext {

    public abstract Sound getHurtSound();

    public abstract double getWidth();
    public abstract double getHeight();

    @SuppressWarnings("ConstantConditions")
    public PacketContainer onMove(PacketContainer playerPacket, PacketContainer mobPacket) {
        return mobPacket;
    }

    public PacketContainer onSpawn(PacketContainer spawnMob){
        return spawnMob;
    }

    public enum Context{
        DEFAULT(null, new DefaultContext()),
        SHULKER(EntityType.SHULKER, new DefaultContext(Sound.ENTITY_SHULKER_HURT, 1, 1)),
        POLAR_BEAR(EntityType.POLAR_BEAR, new DefaultContext(Sound.ENTITY_POLAR_BEAR_HURT, 1.4, 1.4)),
        GUARDIAN(EntityType.GUARDIAN, new GuardianContext()),
        PHANTOM(EntityType.PHANTOM, new PhantomContext()),
        BEE(EntityType.BEE, new BeeContext()),
        BAT(EntityType.BAT, new BatContext()),
        WARDEN(EntityType.WARDEN, new DefaultContext(Sound.ENTITY_WARDEN_HURT, 2.9, 0.9)),
        LLAMA(EntityType.LLAMA, new LlamaContext());

        private final EntityType type;
        private final DisguiseContext context;

        Context(EntityType type, DisguiseContext context){
            this.type = type;
            this.context = context;
        }

        public EntityType getType() {
            return type;
        }

        public DisguiseContext getContext() {
            return context;
        }

    }

    public static DisguiseContext getContextByType(EntityType name){
        DisguiseContext context;
        try {
            context = Arrays.stream(Context.values()).filter(x->x.getType()==name).toList().get(0).getContext();
        }catch(IndexOutOfBoundsException ev){
            context = new DefaultContext();
        }
        return context;
    }

}
