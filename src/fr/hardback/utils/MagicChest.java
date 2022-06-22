package fr.hardback.utils;

import fr.hardback.spigot.tools.hologram.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class MagicChest{

    public static void load(Player player){
        Location location = new Location(Bukkit.getWorld("world"), -0.516, 100.0, -12.592);

        Block block = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).getBlock();
        block.setType(Material.CHEST);

        HologramManager.send(location.add(new Vector(0, -0.5, 0)), Arrays.asList(ChatColor.DARK_PURPLE + "Coffre Magique"));
    }

    public static void onInteract(PlayerInteractEvent event){
        if(event.getClickedBlock().getType() == Material.CHEST) event.getPlayer().sendMessage(ChatColor.RED + "Coffre magique en développent =)");
    }
}
