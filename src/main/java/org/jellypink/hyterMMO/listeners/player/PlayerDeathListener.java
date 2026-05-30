package org.jellypink.hyterMMO.listeners.player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import org.jellypink.hyterMMO.Main;
import org.jellypink.hyterMMO.models.ZoneType;
import org.jellypink.hyterMMO.utils.MessageUtils;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Location wgLocation = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regionManager == null) return;

        ApplicableRegionSet applicableRegions = regionManager.getApplicableRegions(wgLocation.toVector().toBlockPoint());

        StateFlag.State isPluginRegion = applicableRegions.queryValue(null, Flags.SNOWMAN_TRAILS);

        if (isPluginRegion != StateFlag.State.ALLOW) {
            handleWildernessDeath(player, event);
            return;
        }

        // call methods
        ZoneFlagType PlayerZone = ZoneFlagType.getActiveZone(applicableRegions);
        String globalAlert = String.format("&8[&4Black Zone&8] &cThe player &e%s &chas been killed.", player.getName());

        if (PlayerZone != null){
            switch (PlayerZone) {
                case GREEN:
                    event.setKeepInventory(true);
                    event.setKeepLevel(true);
                    event.getDrops().clear();
                    event.setDroppedExp(0);

                    player.sendMessage(MessageUtils.getColoredMessage("&a[MMO] You died in a green zone. your butty is save."));
                    break;
                case YELLOW:
                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    event.setKeepLevel(false);

                    player.sendMessage(MessageUtils.getColoredMessage("&e[MMO] You died in Yellow zone. your butty is safe."));
                    break;
                case RED:
                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    event.setKeepLevel(true);
                    break;
                case BLACK:
                    event.setKeepInventory(false);
                    Player playerdeath = player.getPlayer();
                    org.bukkit.Location location = player.getLocation();
                    playerdeath.getWorld().strikeLightningEffect(location);

                    player.getServer().broadcastMessage(MessageUtils.getColoredMessage(globalAlert));
                    break;
            }
        }
    }

    private void handleWildernessDeath(Player player, PlayerDeathEvent event) {
        player.sendMessage(MessageUtils.getColoredMessage("&7How do you get there?"));
    }
}

