package fr.hardback.utils.npc;

import fr.hardback.Hub;
import fr.hardback.spigot.tools.npc.AbstractNPC;
import fr.hardback.spigot.tools.npc.NPCList;
import fr.hardback.utils.inventory.gui.main.GuiMain;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class NPCMain extends AbstractNPC {

    public NPCMain(Player player, JavaPlugin plugin) {
        super(player, plugin);
    }

    @Override
    public void execute(NPCList npcList) {
        NPC npc = this.getNpcLib().createNPC(Arrays.asList(npcList.getName(), NPCList.SUB_TITLE));
        npc.setLocation(npcList.getLocation());
        this.ids.add(npc.getId());
        npc.create();
        npc.show(this.getPlayer());
    }

    @Override
    public void onNPCInteractEvent(NPCInteractEvent event) {
        if(event.getNPC().getId().equals(ids.first())) new GuiMain(Hub.getInstance(), event.getWhoClicked()).display();
    }
}
