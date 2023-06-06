package org.ssm.untitled.DisguisesV2;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketHelper {

    public static void destroyPlayer(ProtocolManager manager, Player toDestroy){
        PacketContainer destroyPlayer = manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPlayer.getIntLists().write(0, List.of(toDestroy.getEntityId()));
        for (Player check : Bukkit.getOnlinePlayers()){
            if (toDestroy == check){
                continue;
            }
            manager.sendServerPacket(check, destroyPlayer);
        }
    }

    public static void summonDisguise(ProtocolManager manager, Player player, DisguiseData data){
        Location spawnLocation = player.getLocation();
        PacketContainer spawnEntity = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnEntity.getIntegers().write(0, data.getEntityIdentity());
        spawnEntity.getUUIDs().write(0, data.getEntityUUID());
        spawnEntity.getEntityTypeModifier().write(0, data.getType());
        spawnEntity.getBytes().write(0, (byte)spawnLocation.getYaw());
        spawnEntity.getBytes().write(1, (byte)spawnLocation.getPitch());
        spawnEntity.getDoubles().write(0, spawnLocation.getX()).write(1, spawnLocation.getY()).write(2, spawnLocation.getZ());
        for (Player check : Bukkit.getOnlinePlayers()){
            if (player == check){
                continue;
            }
            data.getContext().onSpawn(spawnEntity);
            manager.sendServerPacket(check, spawnEntity);
        }
    }

    public static void playRedEffect(ProtocolManager manager, int target){
        PacketContainer container = manager.createPacket(PacketType.Play.Server.DAMAGE_EVENT);
        container.getIntegers().write(0, target);
        container.getIntegers().write(1, 0);
        manager.broadcastServerPacket(container);
    }

    public static void destroyDisguiseEntity(ProtocolManager manager, DisguiseData data){
        PacketContainer destroyDisguise = manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyDisguise.getIntLists().write(0, List.of(data.getEntityIdentity()));
        manager.broadcastServerPacket(destroyDisguise);
    }
}
