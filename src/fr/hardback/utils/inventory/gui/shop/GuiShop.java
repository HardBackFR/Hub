package fr.hardback.utils.inventory.gui.shop;

import fr.hardback.Hub;
import fr.hardback.bungee.core.rank.RankUnit;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.commons.data.AccountData;
import fr.hardback.spigot.tools.discord.DiscordManager;
import fr.hardback.spigot.tools.gui.AbstractGui;
import fr.hardback.spigot.tools.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GuiShop extends AbstractGui {

    protected final Hub instance;
    protected final AccountData account;

    public GuiShop(Hub instance, Player player) {
        super(instance, player);

        this.instance = instance;
        this.account = DatabaseManager.REDIS.getAccountData(player.getUniqueId());
    }

    @Override
    public void display(){
        final Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Menu de la boutique");

        inventory.setItem(11, new ItemBuilder(Material.GOLD_INGOT).setName(RankUnit.VIP.getPrefix()).setLore(Arrays.asList(ChatColor.DARK_GRAY + "Grade", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.GRAY + " Acheter de grade pour avoir les avantages VIP !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", (this.account.getRank().getPower() >= RankUnit.VIP.getPower() ? ChatColor.RED + "Vous possédez déjà ce grade !" : ChatColor.YELLOW + "-> 5000 Crédits ou 15000 Coins"), " ", ChatColor.YELLOW + "» " + ChatColor.GREEN + "Cliquez pour acheter")).toItemStack());
        inventory.setItem(15, new ItemBuilder(Material.GOLD_BLOCK).setName(RankUnit.VIPP.getPrefix()).setLore(Arrays.asList(ChatColor.DARK_GRAY + "Grade", " ", ChatColor.WHITE + "" + ChatColor.BOLD + "Description", " ", ChatColor.GRAY + " Acheter de grade pour avoir les avantages VIP+ !", " ", ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations", (this.account.getRank().getPower() >= RankUnit.VIPP.getPower() ? ChatColor.RED + "Vous possédez déjà ce grade !" : ChatColor.YELLOW + "-> 10000 Crédits ou 50000 Coins"), " ", ChatColor.YELLOW + "» " + ChatColor.GREEN + "Cliquez pour acheter")).toItemStack());

        this.instance.getServer().getScheduler().runTask(this.instance, () -> this.getPlayer().openInventory(inventory));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || itemStack.getItemMeta() == null) return;

        if(event.getView().getTitle().equalsIgnoreCase("Menu de la boutique")){
            event.setCancelled(true);

            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(RankUnit.VIP.getPrefix())){
                this.payment(account, player, 5000.0, RankUnit.VIP);
            }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(RankUnit.VIPP.getPrefix())){
                this.payment(account, player, 10000.0, RankUnit.VIPP);
            }
        }
    }

    private void payment(AccountData account, Player player, Double amount, RankUnit rank){
        if(account.getRank().getPower() >= rank.getPower()){
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "Votre grade est déjà supérieur à celui ci !");
        }else{
            if(account.getCredits() < amount){
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "Il vous manque " + (amount - this.account.getCredits()) + " crédits pour acheter ce grade !");
            }else{
                player.closeInventory();
                this.account.setRank(rank);
                this.account.setCredits(this.account.getCredits() - amount);
                DatabaseManager.REDIS.setAccountData(player.getUniqueId(), account);
                player.sendMessage(ChatColor.GREEN + "Merci de votre achat !");
                DiscordManager.send(player.getName() + " a acheté le grade " + rank.getName());
            }
        }
    }
}
