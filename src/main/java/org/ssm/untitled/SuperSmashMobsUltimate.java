package org.ssm.untitled;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.ssm.untitled.DisguisesV2.DisguiseHandler;
import org.ssm.untitled.DisguisesV2.DisguiseListener;

import java.util.HashMap;
import java.util.Random;

public final class SuperSmashMobsUltimate extends JavaPlugin implements Listener {

    private static ProtocolManager manager;
    private static JavaPlugin plugin;
    private static final Random random = new Random();


    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new DisguiseListener(), this);
        manager = ProtocolLibrary.getProtocolManager();
        DisguiseListener.startListening(manager);
        this.getCommand("mob").setExecutor((commandSender, command, label, args) -> {
            if (!label.equalsIgnoreCase("mob")){
                return true;
            }
            if (!(commandSender instanceof Player)){
                return true;
            }
            EntityType MOBBIN = EntityType.valueOf(args[0].toUpperCase());
            DisguiseHandler.setDisguise(manager, (Player)commandSender, MOBBIN);
            return true;
        });
    }

    @Override
    public void onDisable() {
        plugin = null;

    }

    public static JavaPlugin getInstance() {
        return plugin;
    }

    public static ProtocolManager getProtocolManager(){
        return manager;
    }

    @EventHandler
    public void onJoin(PlayerInteractEvent e) {
        Player player = e.getPlayer();


        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COAL) {
            DisguiseHandler.setDisguise(manager, player, EntityType.GIANT);
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND) {
            DisguiseHandler.setDisguise(manager, player, EntityType.AXOLOTL);
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMERALD){
            Pig pig = (Pig)player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
            e.getPlayer().addPassenger(pig);
        }
    }


    public static Random getRandom() {
        return random;
    }
}
