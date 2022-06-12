package fr.hardback.utils.npc;

import fr.hardback.Hub;
import fr.hardback.spigot.tools.npc.NPCList;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class NPCMain implements Listener {

    private final Hub instance;

    private final SortedSet<String> ids;
    private final NPCLib npcLib;

    public NPCMain(Hub instance) {
        this.instance = instance;
        this.ids = new TreeSet<>();
        this.npcLib = new NPCLib(this.instance);

        Bukkit.getPluginManager().registerEvents(this, this.instance);
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event){
        if(event.getNPC().getId().equals(this.ids.first())) new GuiMain(this.instance, event.getWhoClicked()).display();
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        NPC npc = npcLib.createNPC(Arrays.asList(NPCList.NAVIGATEUR.getName(), NPCList.SUB_TITLE));
        NPCList npcList = NPCList.NAVIGATEUR;
        npc.setSkin(new Skin(npcList.getTexture(), npcList.getSignature()));
        npc.setLocation(npcList.getLocation());
        npc.create();
        npc.show(event.getPlayer());

        this.ids.add(npc.getId());
    }
}

