package org.ssm.untitled.DisguisesV2;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.ssm.untitled.PlayerUtil;
import org.ssm.untitled.SuperSmashMobsUltimate;

import java.util.List;
import java.util.UUID;

public class DisguiseListener implements Listener {

    private static ProtocolManager _manager;
    private static final ImmutableList<PacketType> movementPackets = ImmutableList.of(
            PacketType.Play.Server.REL_ENTITY_MOVE,
            PacketType.Play.Server.REL_ENTITY_MOVE_LOOK,
            PacketType.Play.Server.ENTITY_LOOK,
            PacketType.Play.Server.ENTITY_TELEPORT,
            PacketType.Play.Server.ENTITY_HEAD_ROTATION
    );

    public static void startListening(ProtocolManager manager) {
        _manager = manager;
        startMovementListening();
        startAttackListening();
        startMiscListening();
    }

    private static void startMiscListening() {
        _manager.addPacketListener(new PacketAdapter(SuperSmashMobsUltimate.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.ENTITY_DESTROY) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                if (packet.getType() == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
                    UUID uuid = packet.getUUIDs().read(0);
                    Player player = Bukkit.getPlayer(uuid);
                    if (DisguiseHandler.hasDisguise(uuid)) {
                        PacketHelper.summonDisguise(_manager, player, DisguiseHandler.getDisguiseData(uuid));
                        event.setCancelled(true);
                    }
                }else if (packet.getType() == PacketType.Play.Server.ENTITY_DESTROY) {
                    List<Integer> list = packet.getIntLists().read(0);
                    for (Integer entityIdentity : list) {
                        if (PlayerUtil.getPlayerById(entityIdentity) == null) {
                            continue;
                        }
                        Player player = PlayerUtil.getPlayerById(entityIdentity);
                        if (!DisguiseHandler.hasDisguise(player.getUniqueId())) {
                            continue;
                        }
                        PacketHelper.destroyDisguiseEntity(_manager, DisguiseHandler.getDisguiseData(player.getUniqueId()));
                    }
                }
            }
        });

    }

    private static void startMovementListening() {
        _manager.addPacketListener(new PacketAdapter(SuperSmashMobsUltimate.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_HEAD_ROTATION, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.ENTITY_TELEPORT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket().deepClone();
                event.setPacket(packet);
                if (movementPackets.contains(packet.getType())) {
                    handleMovement(event, packet);
                }
            }
        });
    }

    private static void startAttackListening() {
        _manager.addPacketListener(new PacketAdapter(SuperSmashMobsUltimate.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.DAMAGE_EVENT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket().deepClone();
                event.setPacket(packet);

                WrappedEnumEntityUseAction wrappedAction = packet.getEnumEntityUseActions().read(0);
                EnumWrappers.EntityUseAction action = wrappedAction.getAction();
                if (action == EnumWrappers.EntityUseAction.ATTACK) { // Melee
                    DisguiseData data = DisguiseHandler.getDisguiseDataFromEntityId(packet.getIntegers().read(0));
                    if (data == null) {
                        return;
                    }
                    if (Bukkit.getPlayer(data.getOwner()).getGameMode() == GameMode.CREATIVE){
                        return;
                    }
                    packet.getIntegers().write(0, Bukkit.getPlayer(data.getOwner()).getEntityId());
                    event.setPacket(packet);
//                    Sound sound = data.getContext().getHurtSound();
                    Location packetLocation = event.getPlayer().getLocation();
//                    packetLocation.getWorld().playSound(packetLocation, sound, 1, 1);
//                    PacketHelper.playRedEffect(_manager, data.getEntityIdentity());
                }
            }
            @Override
            public void onPacketSending(PacketEvent event){
                PacketContainer packet = event.getPacket().deepClone();
                event.setPacket(packet);

                if (packet.getType() == PacketType.Play.Server.DAMAGE_EVENT){
                    int damageeId = packet.getIntegers().read(0);
                    Player player = PlayerUtil.getPlayerById(damageeId);
                    if (player == null){
                        return;
                    }
                    if (!DisguiseHandler.hasDisguise(player.getUniqueId())){
                        return;
                    }
                    DisguiseData data = DisguiseHandler.getDisguiseData(player.getUniqueId());
                    Sound sound = data.getContext().getHurtSound();
                    Location packetLocation = event.getPlayer().getLocation();
                    packetLocation.getWorld().playSound(packetLocation, sound, 1, 1);
                    packet.getIntegers().write(0, data.getEntityIdentity());
                    event.setPacket(packet);
                }
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private static void handleMovement(PacketEvent event, PacketContainer packet) {
        int entid = packet.getIntegers().read(0);
        if (PlayerUtil.getPlayerById(entid) == null) {
            return;
        }
        Player packetSender = PlayerUtil.getPlayerById(entid);
        UUID uuid = packetSender.getUniqueId();
        if (!DisguiseHandler.hasDisguise(uuid)) {
            return;
        }
        PacketContainer toSend = packet.deepClone();
        toSend.getIntegers().write(0, DisguiseHandler.getDisguiseData(uuid).getEntityIdentity());
        if (toSend.getType() != PacketType.Play.Server.ENTITY_HEAD_ROTATION) {
            toSend.getBooleans().write(0, toSend.getBooleans().read(0));
        }
        DisguiseData data = DisguiseHandler.getDisguiseData(uuid);
        PacketContainer mobContext = data.getContext().onMove(packet, toSend);
        _manager.broadcastServerPacket(mobContext);
    }

    private static void handleAttack(PacketEvent event, PacketContainer packet) {

    }

//    @EventHandler
//    private void join(PlayerJoinEvent event) {
//        if (!DisguiseHandler.hasDisguise(event.getPlayer().getUniqueId())) {
//            return;
//        }
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                PacketHelper.summonDisguise(SuperSmashMobsUltimate.getProtocolManager(), event.getPlayer(), DisguiseHandler.getDisguiseData(event.getPlayer().getUniqueId()));
//                PacketHelper.destroyPlayer(SuperSmashMobsUltimate.getProtocolManager(), event.getPlayer());
//            }
//        }.runTaskLater(SuperSmashMobsUltimate.getInstance(), 1);
//    }
//
//    @EventHandler
//    private void leave(PlayerQuitEvent event) {
//        PacketHelper.destroyDisguiseEntity(SuperSmashMobsUltimate.getProtocolManager(), DisguiseHandler.getDisguiseData(event.getPlayer().getUniqueId()));
//    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskLater(SuperSmashMobsUltimate.getInstance(), 1);
    }

    @EventHandler
    public void ihateyoumichaela(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        DisguiseData data = DisguiseHandler.getDisguiseDataFromEntityId(player.getEntityId());
        PacketHelper.playRedEffect(_manager, data.getEntityIdentity());
        Sound sound = data.getContext().getHurtSound();
        player.getWorld().playSound(player.getLocation(), sound, 1, 1);


    }
}
