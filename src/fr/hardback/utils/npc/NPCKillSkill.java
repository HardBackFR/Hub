package fr.hardback.utils.npc;

import dev.sergiferry.playernpc.api.NPC;
import fr.hardback.Hub;
import fr.hardback.spigot.tools.npc.NPCList;
import fr.hardback.spigot.tools.npc.NPCManager;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NPCKillSkill implements Listener {

    private final Hub instance;

    public NPCKillSkill(Hub instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onNPCInteract(NPC.Events.Interact event){
        if(event.getNPC().getLocation() == NPCList.KILLSKILL.getLocation()) new GuiMain(this.instance, event.getPlayer()).display();
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        NPCManager.setup(event.getPlayer(), Hub.getInstance(), NPCList.KILLSKILL);
    }
}



