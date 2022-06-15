package fr.hardback.utils.inventory.gui.cosmetics;

import fr.hardback.Hub;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.spigot.tools.color.ColorUtils;
import fr.hardback.spigot.tools.gui.AbstractGui;
import fr.hardback.spigot.tools.head.CustomHead;
import fr.hardback.spigot.tools.item.ItemBuilder;
import fr.hardback.spigot.tools.pets.Pets;
import fr.hardback.spigot.tools.pets.PetsManager;
import fr.hardback.spigot.tools.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Arrays;

public class GuiCosmetics extends AbstractGui {

    protected Hub instance;
    public static PetsManager petsManager;

    public GuiCosmetics(Hub instance, Player player) {
        super(instance, player);

        this.instance = instance;
    }

    @Override
    public void display(){
        final Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Menu des cosmétiques");

        this.setLine(0, 9, inventory, new ItemBuilder(Material.STAINED_GLASS_PANE, ColorUtils.getRandomDyeColor().getData()).toItemStack());
        inventory.addItem(new ItemBuilder(Material.FIREWORK).setName(ChatColor.RED + "Firework").setLore(Arrays.asList(ChatColor.DARK_GRAY + "Projectile", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.GRAY + " Lance un feu d'artifice de couleur aléatoire !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", ChatColor.YELLOW + "-> Minimum VIP", " ", ChatColor.YELLOW + "» " + ChatColor.GREEN + "Cliquez " + ChatColor.GRAY + "pour l'utiliser")).toItemStack());
        inventory.addItem(new ItemBuilder(CustomHead.create(Pets.RubiksCube.getHeadURL())).setName(Pets.RubiksCube.getName()).setLore(Arrays.asList(ChatColor.DARK_GRAY + "Pets", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.GRAY + " Fait apparaître un Rubik'sCube qui te suis !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", ChatColor.YELLOW + "-> Minimum VIP", " ", ChatColor.YELLOW + "» " + ChatColor.GREEN + "Cliquez " + ChatColor.GRAY + "pour l'utiliser")).toItemStack());
        this.setLine(18, 26, inventory, new ItemBuilder(Material.STAINED_GLASS_PANE, ColorUtils.getRandomDyeColor().getData()).toItemStack());

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
                if(isVip(player)){
                    player.getInventory().addItem(new ItemBuilder(Material.FIREWORK).setName(ChatColor.RED + "Firework").toItemStack());
                }else{
                    player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Vous devez être au minimum " + ChatColor.RESET + "" + RankUnit.VIP.getPrefix() + "" + ChatColor.RED + "" + ChatColor.ITALIC + " pour pouvoir l'utiliser !");
                }
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(Pets.RubiksCube.getName())){
                player.closeInventory();
                if(isVip(player)){
                    petsManager = new PetsManager(player, Pets.RubiksCube);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.533F);
                    new ParticleBuilder(ParticleEffect.SMOKE_LARGE, player.getLocation()).setOffsetY(1.0F).setSpeed(0.1F).display();
                }else{
                    player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Vous devez être au minimum " + ChatColor.RESET + "" + RankUnit.VIP.getPrefix() + "" + ChatColor.RED + "" + ChatColor.ITALIC + " pour pouvoir l'utiliser !");
                }
            }
        }
    }

    private boolean isVip(Player player){
        return DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getRank().getPower() >= RankUnit.VIP.getPower();
    }
}
