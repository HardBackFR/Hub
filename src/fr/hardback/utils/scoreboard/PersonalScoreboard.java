package fr.hardback.utils.scoreboard;

import fr.hardback.commons.DatabaseManager;
import fr.hardback.utils.message.PluginMessaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PersonalScoreboard {
    private Player player;
    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    PersonalScoreboard(Player player){
        this.player = player;
        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "DevPlugin");

        reloadData();
        objectiveSign.addReceiver(player);
        
    }

    public void reloadData(){}

    public void setLines(String ip){
    	PluginMessaging.requestCount("ALL", player);
        objectiveSign.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "HARDBACK");

        objectiveSign.setLine(0, "§1");
        objectiveSign.setLine(1, ChatColor.GOLD + "► Informations");
        objectiveSign.setLine(2, ChatColor.YELLOW + "• Compte: " + ChatColor.WHITE + player.getName());
        objectiveSign.setLine(3, ChatColor.YELLOW + "• Grade: " + DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getRank().getPrefix());
        objectiveSign.setLine(4, "§2");
        objectiveSign.setLine(5, ChatColor.YELLOW + "• Crédits: " + ChatColor.WHITE + DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getCredits());
        objectiveSign.setLine(6, ChatColor.YELLOW + "• Coins: " + ChatColor.WHITE + DatabaseManager.REDIS.getAccountData(player.getUniqueId()).getCoins());
        objectiveSign.setLine(7, "§3");
        objectiveSign.setLine(8, ChatColor.GOLD + "► Informations serveur");
        objectiveSign.setLine(9, ChatColor.YELLOW + "• Lobby: " + ChatColor.WHITE + "#1");
        objectiveSign.setLine(10, ChatColor.YELLOW + "• Joueurs: " + ChatColor.WHITE + PluginMessaging.getPlayerCount("ALL"));
        objectiveSign.setLine(11, "§4");
        objectiveSign.setLine(12, ip);

        objectiveSign.updateLines();
    }

    public void onLogout(){
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }
}