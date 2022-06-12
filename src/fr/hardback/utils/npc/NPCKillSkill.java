package fr.hardback.utils.npc;

import fr.hardback.Hub;
import fr.hardback.spigot.tools.npc.NPCList;
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

public class NPCKillSkill implements Listener {

    private final Hub instance;

    private final NPCLib npcLib;

    public NPCKillSkill(Hub instance) {
        this.instance = instance;
        this.npcLib = new NPCLib(this.instance);

        Bukkit.getPluginManager().registerEvents(this, this.instance);
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event){
        if(event.getNPC().getLocation() == NPCList.KILLSKILL.getLocation()) event.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "KillSkill " + ChatColor.RESET + ChatColor.RED + "En développement !");
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        NPCManager.setup(this.npcLib, NPCList.KILLSKILL, event.getPlayer());
    }
}


