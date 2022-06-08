package fr.hardback.managers.listeners;

import fr.hardback.Hub;
import fr.hardback.bungee.core.rank.RankUnit;
import fr.hardback.commons.DatabaseManager;
import fr.hardback.commons.data.AccountData;
import fr.hardback.spigot.tools.fancymessage.FancyMessage;
import fr.hardback.spigot.tools.firework.CustomFirework;
import fr.hardback.spigot.tools.hologram.Hologram;
import fr.hardback.spigot.tools.title.TitleManager;
import fr.hardback.utils.inventory.gui.cosmetics.GuiCosmetics;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import fr.hardback.utils.inventory.gui.shop.GuiShop;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.EnderChest;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    protected final Hub instance;

    public PlayerListener(Hub instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, () -> DatabaseManager.accountProvider.createAccount(player.getUniqueId(), player.getName()));

        final AccountData accountManager = DatabaseManager.REDIS.getAccountData(player.getUniqueId());

        event.setJoinMessage(null);
        if(accountManager.getRank().isStaff()) {
            player.setAllowFlight(true);
            player.setFlying(true);
            CustomFirework.launchFirework(player);
            event.setJoinMessage(accountManager.getRank().getPrefix() + "" + player.getName() + ChatColor.GOLD + " s'est connecté au Hub !");
        }

        this.setNameTag(player);
        this.instance.getScoreboardManager().onLogin(player);

        TitleManager.sendTitle(player, ChatColor.YELLOW + "HardBack", ChatColor.RED + "Actuellement en maintenance..", 20, 20, 20);

        for(PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        player.teleport(new Location(Bukkit.getWorlds().get(0), -0.495, 100.0, -51.429, -0.1f, -0.2f));
        player.setGameMode(GameMode.ADVENTURE);
        player.setFoodLevel(20);
        player.setWalkSpeed(0.20F);
        player.setFlySpeed(0.15F);
        player.setAllowFlight(false);
        player.setExp(0);
        player.setLevel(0);
        player.setMaxHealth(20.0D);
        player.setHealth(player.getMaxHealth());

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();

        this.instance.getStaticInventory().setInventoryPlayer(player);

        final String[] text = {ChatColor.GOLD + "" + ChatColor.BOLD + "Votre profil", ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-[--------------------]-", ChatColor.GRAY + "Grade: " + accountManager.getRank().getPrefix(), ChatColor.GRAY + "Crédits: " + ChatColor.LIGHT_PURPLE + accountManager.getCredits(), ChatColor.GRAY + "Coins: " + ChatColor.YELLOW + accountManager.getCoins(), ChatColor.GRAY + "Première connexion le " + ChatColor.RED + accountManager.getCreatedAt()};
        new Hologram(text, player.getLocation()).showPlayer(player);

        this.instance.getScheduledExecutorService().schedule(() -> {
            if (!player.isOnline()) return;

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------------------------------------");

            new FancyMessage("Hey ! En attendant viens sur le ffa ! ").color(ChatColor.YELLOW)
                    .then("[Cliquez-ici]").color(ChatColor.GREEN).style(ChatColor.BOLD).command("/join ffa")
                    .formattedTooltip(new FancyMessage("Cliquer pour rejoindre le jeu !").color(ChatColor.YELLOW))
                    .send(player);

            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "--------------------------------------------");
        }, 5, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        event.setQuitMessage(null);
        this.instance.getScoreboardManager().onLogout(event.getPlayer());
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
        if(event.isCancelled()) return;

        event.setFormat(DatabaseManager.REDIS.getAccountData(event.getPlayer().getUniqueId()).getRank().getPrefix() + event.getPlayer().getName() + ChatColor.GRAY + " » " + ChatColor.WHITE + event.getMessage());
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event){
        final Player player = event.getPlayer();
        event.setCancelled(true);

        if(event.getItem() == null)return;

        this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, () -> this.instance.getStaticInventory().doInteraction(player, event.getItem()));

        if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Firework")) {
            CustomFirework.launchFirework(player);
            player.getInventory().remove(player.getInventory().getItemInHand());
        }

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock().getType().equals(Material.ENDER_CHEST)){
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                player.sendMessage(ChatColor.GREEN + "Coffre magique en développement !");
                event.setUseInteractedBlock(Event.Result.DENY);
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        final Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        new GuiMain(this.instance, player).onInventoryClick(event);
        new GuiShop(this.instance, player).onInventoryClick(event);
        new GuiCosmetics(this.instance, player).onInventoryClick(event);
    }

    private void setNameTag(Player player) {
        RankUnit rank = DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getRank();

        this.instance.getScoreboard().getTeam(String.valueOf(rank.getPower())).addPlayer(player);
        Bukkit.getOnlinePlayers().forEach(players -> players.setScoreboard(this.instance.getScoreboard()));
    }
}
