package fr.hardback.utils.npc;

import fr.hardback.Hub;
import fr.hardback.spigot.api.HardBackAPI;
import fr.hardback.spigot.tools.npc.NPCList;
import fr.hardback.spigot.tools.npc.NPCManager;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NPCMain implements Listener {

    private final Hub instance;

    private final NPCLib npcLib;

    public NPCMain(Hub instance) {
        this.instance = instance;
        this.npcLib = new NPCLib(this.instance);

        Bukkit.getPluginManager().registerEvents(this, this.instance);
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event){
        if(event.getNPC().getLocation() == NPCList.NAVIGATEUR.getLocation()) new GuiMain(this.instance, event.getWhoClicked()).display();
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        NPCManager.setup(this.npcLib, NPCList.NAVIGATEUR, event.getPlayer());
    }
}

