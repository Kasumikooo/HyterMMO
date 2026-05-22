package org.example.hyterMMO.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.example.hyterMMO.hyterMMO;
import org.example.hyterMMO.utils.MessageUtils;

public class ZonesCommand {

    private final hyterMMO plugin;

    public ZonesCommand(hyterMMO plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            SendHelp(player);
            return;
        }

        String SubCommand = args[1].toLowerCase();

        switch(SubCommand) {
            case "create":
                // comandos
                handleCreate(player, args);
                break;
            case "delete":
                // comandos
                break;
            case "flag":
                // comandos
                break;
            case "list":
                // comandos
                break;
        }

        return;
    }

    private void handleCreate(Player player, String[] args) {
        if (!player.hasPermission("hyterMMO.admin.zcreate")) {
            player.sendMessage(MessageUtils.getColoredMessage("&cYou do not have permission to use this command!"));
            return;
        }

        if (args.length != 3) {
            player.sendMessage(MessageUtils.getColoredMessage("Usage: /hmmo z create <region_id>"));
            return;
        }

        String regionId = args[2];

        Region selection;
        World weWorld = BukkitAdapter.adapt(player.getWorld());
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));
            selection = session.getSelection(weWorld);
        } catch (IncompleteRegionException e) {
            player.sendMessage(MessageUtils.getColoredMessage("&cYour WorldEdit Selection is Incomplete. Please select two points."));
            return;
        }

        if (selection == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cInvalid or unsupported WorldEdit Selection type. Please make a cuboid (//pos1, //pos2) selection."));
            return;
        }

        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(weWorld);

        if(regionManager == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cInternal error: Could not manage regions for your current world."));
            return;
        }

        if (regionManager.hasRegion(regionId)) {
            player.sendMessage(MessageUtils.getColoredMessage("&cRegion already exists."));
            return;
        }


        ProtectedRegion region = new ProtectedCuboidRegion(regionId, min, max);

        region.setFlag(Flags.PVP, StateFlag.State.DENY);
        String greetingsMessage = MessageUtils.getColoredMessage("&aWelcome to the " + regionId + "green zone!");
        region.setFlag(Flags.GREET_MESSAGE, greetingsMessage);

        region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
        region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);

        regionManager.addRegion(region);

        try {
            regionManager.save();
            player.sendMessage(MessageUtils.getColoredMessage("&aSuccessfully created region " + regionId + "!"));
        } catch (Exception ex) {
            ex.printStackTrace();
            player.sendMessage(MessageUtils.getColoredMessage("&cAn error occurred while saving the region."));
        }

    }



    private void SendHelp(Player player) {
        player.sendMessage(MessageUtils.getColoredMessage("&6====== &bZones Help &6======"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo z create &7- Gestión de regiones del MMO"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo z delete &7- Gestión de monstruos personalizados"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo z list &7- Gestión de minería"));
    }
}
