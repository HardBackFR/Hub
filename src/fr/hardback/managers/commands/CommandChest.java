package fr.hardback.managers.commands;

import fr.hardback.Hub;
import fr.hardback.spigot.tools.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CommandChest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        Inventory inventory = Bukkit.createInventory(null, 54, "Animated Gui");

        new BukkitRunnable(){
            @Override
            public void run() {
                for(int i = 0; i < 54; i++){
                    inventory.setItem(i, new ItemBuilder(Material.values()[new Random().nextInt(Material.values().length)]).setName(ChatColor.GREEN + "Random Item").toItemStack());
                }
            }
        }.runTaskTimer(Hub.getInstance(), 0L, 10L);

        player.openInventory(inventory);
        return false;
    }
}
