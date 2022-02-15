package fr.hardback.utils.inventory.gui.cosmetics;

import fr.hardback.Hub;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.spigot.tools.gui.AbstractGui;
import fr.hardback.spigot.tools.item.ItemBuilder;
import fr.hardback.spigot.tools.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GuiCosmetics extends AbstractGui {

    protected Hub instance;

    public GuiCosmetics(Hub instance, Player player) {
        super(instance, player);

        this.instance = instance;
    }

    @Override
    public void display(){
        final Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Menu des cosmétiques");

        inventory.addItem(new ItemBuilder(Material.FIREWORK).setName(ChatColor.RED + "Firework").setLore(Arrays.asList(ChatColor.DARK_GRAY + "Projectile", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.GRAY + " Lance un feu d'artifice de couleur aléatoire !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", ChatColor.YELLOW + "-> Minimum VIP", " ", ChatColor.YELLOW + "» " + ChatColor.GREEN + "Cliquez " + ChatColor.GRAY + "pour l'utiliser")).toItemStack());

        this.instance.getServer().getScheduler().runTask(this.instance, () -> this.getPlayer().openInventory(inventory));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event){
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        final ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || itemStack.getType() == null || itemStack.getItemMeta() == null) return;

        if(inventory.getName().equalsIgnoreCase("Menu des cosmétiques")){
            event.setCancelled(true);

            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Firework")){
                player.closeInventory();
                if(DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getRank().getPower() >= RankUnit.VIP.getPower()) {
                    player.getInventory().addItem(new ItemBuilder(Material.FIREWORK).setName(ChatColor.RED + "Firework").toItemStack());
                }else {
                    player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Vous devez être au minimum " + ChatColor.RESET + "" + RankUnit.VIP.getPrefix() + "" + ChatColor.RED + "" + ChatColor.ITALIC + " pour pouvoir l'utiliser !");
                }
            }
        }
    }
}
