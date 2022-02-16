package fr.hardback.managers.commands;

import fr.hardback.Hub;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.spigot.tools.hologram.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class CommandSpawnChest implements CommandExecutor {

    protected final Hub instance;

    public CommandSpawnChest(Hub instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return true;

        final Player player = (Player) sender;

        if(!DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getRank().isStaff()){
            player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'éxécuter cette commande !");
            return true;
        }

        final Block block = player.getLocation().getBlock();
        final Block blockBelow = block.getRelative(BlockFace.DOWN);

        blockBelow.setType(Material.ENDER_CHEST);

        final String[] text = {ChatColor.DARK_PURPLE + "Coffre Magique"};
        final Hologram hologram = new Hologram(text, player.getLocation().add(new Vector(0, +0.25, 0)));
        hologram.showPlayer(player);
        return false;
    }
}
