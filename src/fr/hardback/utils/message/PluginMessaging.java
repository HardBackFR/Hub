package fr.hardback.utils.message;

import java.util.HashMap;
import java.util.Map;

import fr.hardback.Hub;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PluginMessaging implements PluginMessageListener{

    private static boolean isListening = false;
    public static String serverName;
    public static int playerNumber = 0;

    public static Map<String, Integer> playerCount = new HashMap<>();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if(subChannel.equals("PlayerCount") && isListening) {
            String server = in.readUTF();
            int count = in.readInt();

            playerCount.put(server, count);
            isListening = true;
        }else if(subChannel.equals("Connect") && isListening) {
            //String server = in.readUTF();
        }
    }

    public static void requestCount(String name, Player player) {
        if(name == null) {
            name = "ALL";
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(name);

        player.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
        isListening = true;
    }

    public static Integer getPlayerCount(String name) {
        return playerCount.get(name);
    }

    public static void connect(String name, Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(name);

        player.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
        isListening = true;
    }

}
