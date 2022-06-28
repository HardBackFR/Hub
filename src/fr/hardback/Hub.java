package fr.hardback;

import fr.hardback.commons.DatabaseManager;
import fr.hardback.managers.Managers;
import fr.hardback.spigot.tools.rank.RankUnit;
import fr.hardback.utils.inventory.StaticInventory;
import fr.hardback.utils.message.PluginMessaging;
import fr.hardback.utils.scoreboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Hub extends JavaPlugin {

    private static Hub instance;

    private World world;

    private StaticInventory staticInventory;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    private Scoreboard scoreboard;

    public static final Map<UUID, FastBoard> boards = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        DatabaseManager.initAllConnection();

        this.saveDefaultConfig();

        this.world = this.getServer().getWorlds().get(0);
        this.world.setGameRuleValue("randomTickSpeed", "0");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("announceAdvancements", "false");
        this.world.setWeatherDuration(0);
        this.world.setDifficulty(Difficulty.PEACEFUL);
        this.world.setPVP(false);
        this.world.setTime(this.getConfig().getLong("time", 6000L));

        this.staticInventory = new StaticInventory(this);

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.executorMonoThread = Executors.newScheduledThreadPool(1);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessaging());

        new Managers(this);

        this.getServer().getScheduler().runTaskTimer(this, () -> {
            for(FastBoard board : boards.values()){
                this.updateBoard(board);
            }
        }, 0, 20);

        for(RankUnit ranks : RankUnit.values()){
            this.scoreboard.registerNewTeam(String.valueOf(ranks.getPower()));
            this.scoreboard.getTeam(String.valueOf(ranks.getPower())).setPrefix(ranks.getPrefix());
        }
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllConnection();
        Bukkit.getOnlinePlayers().forEach(players -> players.kickPlayer(null));
    }

    public static Hub getInstance() {
        return instance;
    }

    public World getWorld() {
        return world;
    }

    public StaticInventory getStaticInventory() {
        return staticInventory;
    }

    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void updateBoard(FastBoard board) {
        PluginMessaging.requestCount("ALL", board.getPlayer());

        board.updateLines(
                "",
                ChatColor.GOLD + "► Informations",
                ChatColor.YELLOW + "• Compte: " + ChatColor.WHITE + board.getPlayer().getName(),
                ChatColor.YELLOW + "• Grade: " + DatabaseManager.REDIS.getAccountData(board.getPlayer().getUniqueId()).getRank().getPrefix(),
                "",
                ChatColor.YELLOW + "• Crédits: " + ChatColor.WHITE + DatabaseManager.REDIS.getAccountData(board.getPlayer().getUniqueId()).getCredits(),
                ChatColor.YELLOW + "• Coins: " + ChatColor.WHITE + DatabaseManager.REDIS.getAccountData(board.getPlayer().getUniqueId()).getCoins(),
                "",
                ChatColor.GOLD + "► Informations serveur",
                ChatColor.YELLOW + "• Lobby: " + ChatColor.WHITE + "#1",
                ChatColor.YELLOW + "• Joueurs: " + ChatColor.WHITE + PluginMessaging.getPlayerCount("ALL"),
                "",
                ChatColor.GOLD + "" + ChatColor.BOLD +"play.hardback.fr"
        );
    }

}
