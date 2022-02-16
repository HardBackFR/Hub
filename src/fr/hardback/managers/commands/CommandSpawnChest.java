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

        final Block playerBlock = player.getLocation().getBlock();
        final Block blockUnder = playerBlock.getRelative(BlockFace.DOWN);

        if(blockUnder.getType().equals(Material.AIR)){
            final List<Block> blockList = Arrays.asList(blockUnder, blockUnder.getRelative(BlockFace.EAST), blockUnder.getRelative(BlockFace.WEST), blockUnder.getRelative(BlockFace.NORTH), blockUnder.getRelative(BlockFace.SOUTH), blockUnder.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST), blockUnder.getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST), blockUnder.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST), blockUnder.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST));

            for(Block block : blockList) block.setType(Material.ENDER_CHEST);

            player.getServer().getScheduler().runTaskLater(this.instance, () -> {
                for(Block block : blockList){
                    if (block != null && block.getType() == Material.BEDROCK) block.setType(Material.AIR);
                }
                blockList.clear();
            }, 2 * 1200L);
        }

        String[] text = {ChatColor.DARK_PURPLE + "Coffre Magique"};
        Hologram hologram = new Hologram(text, player.getLocation().add(new Vector(0, +1, 0)));
        hologram.showPlayer(player);
        return false;
    }
}
