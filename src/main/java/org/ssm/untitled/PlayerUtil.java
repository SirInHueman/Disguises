package org.ssm.untitled;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static Player getPlayerById(int id) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (id == player.getEntityId()) {
                return player;
            }
        }
        return null;
    }
}
