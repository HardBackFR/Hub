package fr.hardback.managers;

import fr.hardback.Hub;
import fr.hardback.managers.listeners.CancelListener;
import fr.hardback.managers.listeners.PlayerListener;
import fr.hardback.utils.MagicChest;
import fr.hardback.utils.npc.NPCEvolution;
import fr.hardback.utils.npc.NPCKillSkill;
import fr.hardback.utils.npc.NPCMain;
import org.bukkit.plugin.PluginManager;

public class Managers {

    protected final Hub instance;

    public Managers(Hub instance) {
        this.instance = instance;

        final PluginManager pluginManager = this.instance.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerListener(this.instance), this.instance);
        pluginManager.registerEvents(new CancelListener(this.instance), this.instance);

        /*
          NPCs
         */
        pluginManager.registerEvents(new NPCMain(this.instance), this.instance);
        pluginManager.registerEvents(new NPCEvolution(this.instance), this.instance);
        pluginManager.registerEvents(new NPCKillSkill(this.instance), this.instance);

        /*
          MagicChest
         */
        pluginManager.registerEvents(new MagicChest(),this.instance);
    }
}
