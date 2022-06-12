package fr.hardback.utils.npc;

import fr.hardback.spigot.tools.npc.NPCList;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class NPCManager {

    public static void setup(NPCLib npcLib, NPCList npcList, Player player){
        NPC npc = npcLib.createNPC(Arrays.asList(npcList.getName(), NPCList.SUB_TITLE));
        npc.setSkin(new Skin(npcList.getTexture(), npcList.getSignature()));
        npc.setLocation(npcList.getLocation());
        npc.create();
        npc.show(player);
    }
}
