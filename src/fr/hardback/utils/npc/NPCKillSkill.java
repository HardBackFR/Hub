package fr.hardback.utils.npc;

import fr.hardback.Hub;
import fr.hardback.spigot.tools.npc.NPCList;
import fr.hardback.spigot.tools.npc.NPCManager;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NPCKillSkill implements Listener {

    private final NPCLib npcLib;

    public NPCKillSkill() {
        this.npcLib = new NPCLib(Hub.getInstance());
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event){
        if(event.getNPC().getLocation() == NPCList.KILLSKILL.getLocation()) event.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "KillSkill " + ChatColor.RESET + ChatColor.RED + "en développement !");
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        NPCManager.setup(this.npcLib, NPCList.KILLSKILL, event.getPlayer());
    }
}


