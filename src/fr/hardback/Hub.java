package fr.hardback;

import fr.hardback.commons.DatabaseManager;
import fr.hardback.managers.Managers;
import fr.hardback.spigot.api.HardBackAPI;
import fr.hardback.spigot.tools.pets.PetManager;
import fr.hardback.spigot.tools.rank.RankUnit;
import fr.hardback.utils.inventory.StaticInventory;
import fr.hardback.utils.message.PluginMessaging;
import fr.hardback.utils.scoreboard.ScoreboardManager;
import net.jitse.npclib.NPCLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Hub extends JavaPlugin {

    private static Hub instance;

    private World world;

    private StaticInventory staticInventory;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    private Scoreboard scoreboard;
    private ScoreboardManager scoreboardManager;

    public PetManager petManager;
    public PetManager playerPetManager;

    @Override
    public void onEnable() {
        instance = this;

        DatabaseManager.initAllConnection();

        this.saveDefaultConfig();

        this.world = this.getServer().getWorlds().get(0);
        this.world.setGameRuleValue("randomTickSpeed", "0");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setWeatherDuration(0);
        this.world.setDifficulty(Difficulty.PEACEFUL);
        this.world.setPVP(false);
        this.world.setTime(this.getConfig().getLong("time", 6000L));

        this.staticInventory = new StaticInventory(this);

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.executorMonoThread = Executors.newScheduledThreadPool(1);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.scoreboardManager = new ScoreboardManager();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessaging());

        new Managers(this);

        for(RankUnit ranks : RankUnit.values()){
            this.scoreboard.registerNewTeam(String.valueOf(ranks.getPower()));
            this.scoreboard.getTeam(String.valueOf(ranks.getPower())).setPrefix(ranks.getPrefix());
        }

        this.petManager = new PetManager();
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllConnection();
        this.petManager.unloadCosmetic();

        Bukkit.getOnlinePlayers().forEach(players -> players.kickPlayer(HardBackAPI.PREFIX + "\n" + ChatColor.RED + "Le serveur redémarre !"));
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

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public PetManager getPlayerPetManager() {
        return playerPetManager;
    }
}
