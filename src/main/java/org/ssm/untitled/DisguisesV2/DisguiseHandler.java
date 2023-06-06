package org.ssm.untitled.DisguisesV2;

import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.ssm.untitled.SuperSmashMobsUltimate;

import java.util.HashMap;
import java.util.UUID;

/**
 * Setting / Getting disguises
 */
public class DisguiseHandler {

    private static final HashMap<UUID, DisguiseData> disguises = new HashMap<>();

    public static DisguiseData getDisguiseData(UUID uuid){
        return disguises.get(uuid);
    }

    public static void setDisguise(ProtocolManager manager, Player owner, EntityType type){
        UUID entityUUID = UUID.randomUUID();
        Integer entityId = SuperSmashMobsUltimate.getRandom().nextInt(1000);
        DisguiseData data = addDisguise(owner, type, entityId, entityUUID);
        PacketHelper.destroyPlayer(manager, owner);
        PacketHelper.summonDisguise(manager, owner, data);
    }

    public static DisguiseData getDisguiseDataFromEntityId(int num){
        return disguises.values().stream().filter(n->n.getEntityIdentity()==num).toList().get(0);
    }

    /**
     * Adds to disguise list
     */
    private static DisguiseData addDisguise(Player player, EntityType type, Integer entityId, UUID uuid){
        if (disguises.containsKey(player.getUniqueId())){
            PacketHelper.destroyDisguiseEntity(SuperSmashMobsUltimate.getProtocolManager(), disguises.get(player.getUniqueId()));
        }
        disguises.remove(player.getUniqueId());
        DisguiseContext context = DisguiseContext.getContextByType(type);
        DisguiseData disguiseData = new DisguiseData(player.getUniqueId(), type, entityId, uuid, context);
        disguises.put(player.getUniqueId(), disguiseData);
        return disguiseData;
    }

    public static void removeDisguiseData(UUID uuid){
        disguises.computeIfPresent(uuid, (u, d)->disguises.remove(u));
    }

    public static boolean hasDisguise(UUID uuid){
        return disguises.containsKey(uuid);
    }
}

