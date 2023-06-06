package org.ssm.untitled.DisguisesV2;

import org.bukkit.entity.EntityType;

import java.util.UUID;

public class DisguiseData {

    private final UUID owner;
    private final EntityType type;
    private final int entityIdentity;
    private final UUID uuid;
    private final DisguiseContext context;

    public DisguiseData(UUID owner, EntityType type, Integer entityIdentity, UUID uuid, DisguiseContext context){
        this.owner = owner;
        this.type = type;
        this.entityIdentity = entityIdentity;
        this.uuid = uuid;
        this.context = context;
    }

    public EntityType getType() {
        return type;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getEntityIdentity() {
        return entityIdentity;
    }

    public UUID getEntityUUID() {
        return uuid;
    }

    public DisguiseContext getContext() {
        return context;
    }
}
