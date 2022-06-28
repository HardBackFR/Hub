package fr.hardback.utils.inventory;

import fr.hardback.Hub;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.commons.data.AccountData;
import fr.hardback.spigot.tools.item.ItemBuilder;
import fr.hardback.utils.inventory.gui.cosmetics.GuiCosmetics;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import fr.hardback.utils.inventory.gui.shop.GuiShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class StaticInventory {

    private final Hub instance;
    private final Map<Integer, ItemStack> items;

    public StaticInventory(Hub instance) {
        this.instance = instance;

        this.items = new HashMap<>();
        this.items.put(0, new ItemBuilder(Material.COMPASS).setName(createTitle("Navigation")).toItemStack());
        this.items.put(1, new ItemBuilder(Material.LEGACY_SKULL_ITEM, 1, (byte) 3).setName(createTitle("Profil")).toItemStack());
        this.items.put(4, new ItemBuilder(Material.GOLD_INGOT).setName(createTitle("Boutique")).toItemStack());
        this.items.put(8, new ItemBuilder(Material.ENDER_CHEST).setName(randomColors() + ChatColor.GRAY + " (Clic-droit)").toItemStack());
    }

    public void doInteraction(Player player, ItemStack itemStack){
        switch (itemStack.getType()) {
            case COMPASS -> new GuiMain(this.instance, player).display();
            case GOLD_INGOT -> new GuiShop(this.instance, player).display();
            case ENDER_CHEST -> new GuiCosmetics(this.instance, player).display();
            default -> {
            }
        }

        if(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Profil" + ChatColor.RESET + "" + ChatColor.GRAY + " (Clic-droit)")){
            AccountData account = DatabaseManager.REDIS.getAccountData(player.getUniqueId());
            player.sendMessage(" ");
            player.sendMessage(ChatColor.GRAY + "» " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Informations" + ChatColor.WHITE + "" + ChatColor.BOLD + " | " + ChatColor.YELLOW + "" + ChatColor.BOLD + "Profil");
            player.sendMessage(" ");
            player.sendMessage(ChatColor.DARK_GRAY + "⯀ " + ChatColor.GOLD + "Grade" + ChatColor.GRAY + " » " + ChatColor.WHITE + account.getRank().getPrefix());
            player.sendMessage(ChatColor.DARK_GRAY + "⯀ " + ChatColor.GOLD + "Crédits" + ChatColor.GRAY + " » " + ChatColor.WHITE +  account.getCredits());
            player.sendMessage(ChatColor.DARK_GRAY + "⯀ " + ChatColor.GOLD + "Coins" + ChatColor.GRAY + " » " + ChatColor.WHITE + account.getCoins());
            player.sendMessage(ChatColor.DARK_GRAY + "⯀ " + ChatColor.GOLD + "Compté crée le" + ChatColor.GRAY + " » " + ChatColor.WHITE +  account.getCreatedAt());
            player.sendMessage(ChatColor.DARK_GRAY + "⯀ " + ChatColor.GOLD + "Lobby" + ChatColor.GRAY + " » " + ChatColor.WHITE + "#1");
            player.sendMessage(" ");
        }
    }

    public void setInventoryPlayer(Player player){
        for(int slots : this.items.keySet()){
            if(this.items.get(slots).getType() == Material.LEGACY_SKULL_ITEM){
                SkullMeta meta = (SkullMeta) this.items.get(slots).getItemMeta();
                Objects.requireNonNull(meta).setOwner(player.getName());

                this.items.get(slots).setItemMeta(meta);
            }

            player.getInventory().setItem(slots, this.items.get(slots));
        }
    }

    private static String createTitle(String text){
        return ChatColor.GOLD + "" + ChatColor.BOLD + text + ChatColor.RESET + "" + ChatColor.GRAY + " (Clic-droit)";
    }

    private String randomColors(){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for(Character characters : "Cosmétiques".toCharArray()){
            stringBuilder.append(ChatColor.getByChar(Integer.toHexString(random.nextInt(16))));
            stringBuilder.append(characters);
        }
        return stringBuilder.toString();
    }
}
