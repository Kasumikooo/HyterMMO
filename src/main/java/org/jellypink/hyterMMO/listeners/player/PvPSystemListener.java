package org.jellypink.hyterMMO.listeners.player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jellypink.hyterMMO.Main;
import org.jellypink.hyterMMO.managers.PvPSystem;
import org.jellypink.hyterMMO.utils.MessageUtils;

public class PvPSystemListener implements Listener {

    private final Main plugin;
    private final PvPSystem pvpSystem;

    public PvPSystemListener(Main plugin, PvPSystem pvpSystem) {
        this.plugin = plugin;
        this.pvpSystem = pvpSystem;
    }

    public ZoneFlagType getPlayerZone(Player player) {
        Location loc = player.getLocation();
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));

        for (ZoneFlagType zone : ZoneFlagType.values()) {
            StateFlag worldGuardFlag = zone.getFlag();

            if (worldGuardFlag != null) {
                if (set.queryState(null, worldGuardFlag) == StateFlag.State.ALLOW) {
                    return zone;
                }
            }
        }
        return ZoneFlagType.BLACK;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        ZoneFlagType attackerZone = getPlayerZone(attacker);
        ZoneFlagType victimZone = getPlayerZone(victim);

        if (attackerZone == ZoneFlagType.GREEN || attackerZone == ZoneFlagType.CITY ||
                victimZone == ZoneFlagType.GREEN || victimZone == ZoneFlagType.CITY) {
            event.setCancelled(true);
            attacker.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &cYou cannot fight in a safe zone!"));
            return;
        }

        if (attackerZone == ZoneFlagType.RED || attackerZone == ZoneFlagType.BLACK) {
            return;
        }

        if (attackerZone == ZoneFlagType.YELLOW && victimZone == ZoneFlagType.YELLOW) {
            if (!pvpSystem.hasPvPEnabled(attacker)) {
                event.setCancelled(true);
                attacker.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &cYou must enable your PvP to attack players!"));
            } else if (!pvpSystem.hasPvPEnabled(victim)) {
                event.setCancelled(true);
                attacker.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + " &cThis player does not have their PvP enabled!"));
            }
        }
    }

    public void updatePlayerNameColor(Player player) {
        String green = ChatColor.GREEN + "● " + ChatColor.WHITE + player.getName();
        String red = ChatColor.RED + "● " + ChatColor.WHITE + player.getName();

        if (pvpSystem.hasPvPEnabled(player)) {
            player.setDisplayName(red);
            player.setPlayerListName(red);
            player.setCustomName(red);
        } else {
            player.setDisplayName(green);
            player.setPlayerListName(green);
            player.setCustomName(green);
        }
    }

    private final java.util.HashMap<java.util.UUID, ZoneFlagType> lastKnownZone = new java.util.HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null || (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())) {
            return;
        }

        Player player = event.getPlayer();
        ZoneFlagType currentZone = getPlayerZone(player);
        ZoneFlagType previousZone = lastKnownZone.get(player.getUniqueId());

        if (previousZone == currentZone) {
            return;
        }

        lastKnownZone.put(player.getUniqueId(), currentZone);
        handleAutomaticPvPStatus(player, currentZone, previousZone);
    }

    private void handleAutomaticPvPStatus(Player player, ZoneFlagType currentZone, ZoneFlagType previousZone) {

        if (currentZone == ZoneFlagType.RED || currentZone == ZoneFlagType.BLACK) {
            if (!pvpSystem.hasPvPEnabled(player)) {
                pvpSystem.setPvP(player, true);
                player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + "&cYou entered a Dangerous Zone. Your PvP status has been forced ON!"));
            }
        }

        else if (currentZone == ZoneFlagType.GREEN || currentZone == ZoneFlagType.CITY) {
            if (pvpSystem.hasPvPEnabled(player)) {
                pvpSystem.setPvP(player, false);
                player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + "&aYou left a dangerous Zone. Your PvP status has been automatically disabled."));
            }
        }

        else if (currentZone == ZoneFlagType.YELLOW) {

            if (previousZone == ZoneFlagType.RED || previousZone == ZoneFlagType.BLACK) {
                if (pvpSystem.hasPvPEnabled(player)) {
                    pvpSystem.setPvP(player, false);
                    player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + "&eYou entered the Yellow Zone. PvP set to neutral. Use /pvp to toggle."));
                }
            }
        }

        updatePlayerNameColor(player);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
        player.setCustomName(null);

        pvpSystem.removePlayerOnQuit(player);
        lastKnownZone.remove(player.getUniqueId());
    }
}