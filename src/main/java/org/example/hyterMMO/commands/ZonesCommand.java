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

public class ZonesCommand implements CommandExecutor {

    private final hyterMMO plugin;

    public ZonesCommand(hyterMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        Player player = (Player) sender;
        if(args.length == 0) {
            SendHelp(player);
            return true;
        }

        String SubCommand = args[0].toLowerCase();

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

        return true;
    }

    private void handleCreate(Player player, String[] args) {
        if (player.hasPermission("hyterMMO.admin.zcreate")) {
            player.sendMessage(MessageUtils.ConsoleMessage());
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
        player.sendMessage(MessageUtils.getColoredMessage("Zones Help"));
        player.sendMessage(MessageUtils.getColoredMessage("--"));
        player.sendMessage(MessageUtils.getColoredMessage("--"));
        player.sendMessage(MessageUtils.getColoredMessage("--"));
    }
}
