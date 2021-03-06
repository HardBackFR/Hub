package fr.hardback.utils.inventory.gui.main;

import fr.hardback.Hub;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.spigot.tools.gui.AbstractGui;
import fr.hardback.spigot.tools.item.ItemBuilder;
import fr.hardback.utils.inventory.gui.cosmetics.GuiCosmetics;
import fr.hardback.utils.inventory.gui.shop.GuiShop;
import fr.hardback.utils.message.PluginMessaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class GuiMain extends AbstractGui {

    protected final Hub instance;

    public GuiMain(Hub instance, Player player) {
        super(instance, player);

        this.instance = instance;
    }

    @Override
    public void display(){
        /*
        PluginMessaging.requestCount("evolution", getPlayer());
        PluginMessaging.requestCount("killskill", getPlayer());
         */
        final Inventory inventory = Bukkit.createInventory(null, 9 * 6, "Menu Principal");

        inventory.setItem(0, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(1, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(9, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());

        inventory.setItem(7, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(8, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(17, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());

        if(DatabaseManager.REDIS.getAccountData(getPlayer().getUniqueId()).getRank().isStaff()) inventory.setItem(13, new ItemBuilder(Material.BARRIER).setName(ChatColor.DARK_RED + "Serveur STAFF").setLore(Arrays.asList("", ChatColor.RED + "Seulement les membres du STAFF", ChatColor.RED + "on le droit ?? l'acc??s ?? ce serveur !")).toItemStack());
        inventory.setItem(20, new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.GREEN + "Evolution").setLore(Arrays.asList(ChatColor.DARK_GRAY + "PVP, Equipe", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.RED + " En d??veloppement !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", ChatColor.GRAY + "-> Joueurs connect??s: " + ChatColor.GREEN + PluginMessaging.getPlayerCount("evolution") + ChatColor.DARK_GRAY + "/375", ChatColor.GRAY + "-> Version du serveur: " + ChatColor.YELLOW + "1.8", " ", ChatColor.YELLOW + "?? " + ChatColor.GREEN + "Cliquez pour rejoindre")).toItemStack());
        inventory.setItem(24, new ItemBuilder(Material.ANVIL).setName(ChatColor.GREEN + "KillSkill").setLore(Arrays.asList(ChatColor.DARK_GRAY + "PVP, Solo", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.RED + " En d??veloppement !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", ChatColor.GRAY + "-> Joueurs connect??s: " + ChatColor.GREEN + PluginMessaging.getPlayerCount("killskill") + ChatColor.DARK_GRAY + "/375", ChatColor.GRAY + "-> Version du serveur: " + ChatColor.YELLOW + "1.8", " ", ChatColor.YELLOW + "?? " + ChatColor.GREEN + "Cliquez pour rejoindre")).toItemStack());
        inventory.setItem(48, new ItemBuilder(Material.LEGACY_SKULL_ITEM, 1, (byte) 3).setSkullOwner(this.getPlayer().getName()).setName(ChatColor.AQUA + "Profil").setLore(Collections.singletonList(ChatColor.GRAY + "Cliquer pour acc??der ?? votre profil")).toItemStack());
        inventory.setItem(48, new ItemBuilder(Material.GOLD_INGOT).setName(ChatColor.AQUA + "Boutique").setLore(Collections.singletonList(ChatColor.GRAY + "Cliquer pour acc??der ?? la boutique")).toItemStack());
        inventory.setItem(50, new ItemBuilder(Material.ENDER_CHEST).setName(ChatColor.AQUA + "Cosm??tiques").setLore(Collections.singletonList(ChatColor.GRAY + "Cliquer pour acc??der ?? vos cosm??tiques")).toItemStack());
        inventory.setItem(42, new ItemBuilder(Material.PAPER).setName(ChatColor.AQUA + "Informations").setLore(Arrays.asList(ChatColor.DARK_GRAY + "Site internet : " + ChatColor.GRAY + "https://hardback.fr", ChatColor.DARK_GRAY + "Boutique : " + ChatColor.GRAY + "https://hardback.fr/shop", "", ChatColor.DARK_GRAY + "Discord : " + ChatColor.GRAY + "https://hardback.fr/discord")).toItemStack());
        inventory.setItem(43, new ItemBuilder(Material.MINECART).setName(ChatColor.AQUA + "Param??tres").setLore(Collections.singletonList(ChatColor.GRAY + "Cliquer pour acc??der ?? vos param??tres")).toItemStack());

        inventory.setItem(36, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(45, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(46, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());

        inventory.setItem(52, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(53, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());
        inventory.setItem(44, new ItemBuilder(Material.GLASS_PANE, 1, (byte) 0).setName(" ").toItemStack());

        this.instance.getServer().getScheduler().runTask(this.instance, () -> this.getPlayer().openInventory(inventory));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event){
        event.setCancelled(true);
        final Player player = (Player) event.getWhoClicked();
        final ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || itemStack.getItemMeta() == null) return;

        if(event.getView().getTitle().equalsIgnoreCase("Menu Principal")){
            event.setCancelled(true);

            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Evolution")){
                player.closeInventory();
                PluginMessaging.connect("evolution", player);
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "KillSkill")) {
                player.closeInventory();
                PluginMessaging.connect("killskill", player);
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "KillSkill")) {
                player.closeInventory();
                PluginMessaging.connect("killskill", player);
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_RED + "Serveur STAFF")) {
                player.closeInventory();
                PluginMessaging.connect("admin", player);
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Boutique")) {
                player.closeInventory();
                new GuiShop(this.instance, player).display();
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Cosm??tiques")) {
                player.closeInventory();
                new GuiCosmetics(this.instance, player).display();
            }
        }
    }
}
