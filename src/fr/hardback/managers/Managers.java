package fr.hardback.managers;

import fr.hardback.Hub;
import fr.hardback.managers.commands.CommandSpawnChest;
import fr.hardback.managers.listeners.CancelListener;
import fr.hardback.managers.listeners.PlayerListener;
import org.bukkit.plugin.PluginManager;

public class Managers {

    protected final Hub instance;

    public Managers(Hub instance) {
        this.instance = instance;

        final PluginManager pluginManager = this.instance.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerListener(this.instance), this.instance);
        pluginManager.registerEvents(new CancelListener(this.instance), this.instance);


        this.instance.getCommand("spawnchest").setExecutor(new CommandSpawnChest(this.instance));
    }
}
