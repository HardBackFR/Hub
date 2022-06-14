package fr.hardback.utils.npc;

import fr.hardback.Hub;
import fr.hardback.spigot.tools.npc.NPCList;
import fr.hardback.spigot.tools.npc.NPCManager;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class NPCEvolution implements Listener {

    private final NPCLib npcLib;

    public NPCEvolution() {
        this.npcLib = new NPCLib(Hub.getInstance());
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event){
        if(event.getNPC().getLocation() == NPCList.EVOLUTION.getLocation()) event.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Evolution " + ChatColor.RESET + ChatColor.RED + "en développement !");
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        NPCManager.setup(this.npcLib, NPCList.EVOLUTION, event.getPlayer());
    }
}


