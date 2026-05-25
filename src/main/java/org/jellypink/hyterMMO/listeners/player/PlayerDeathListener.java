package org.jellypink.hyterMMO.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import  org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent pde) {

        Player player = pde.getEntity();

    }

}
