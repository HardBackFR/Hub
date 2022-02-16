package fr.hardback.managers.listeners;

import fr.hardback.Hub;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CancelListener implements Listener {

    protected final Hub instance;

    public CancelListener(Hub instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){ event.setCancelled(true); }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){ event.setCancelled(true); }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        event.setCancelled(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        event.setCancelled(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        event.setCancelled(true);
        event.setCancelled(!event.getEntity().getType().equals(EntityType.PLAYER));
        event.setCancelled(event.getCause().equals(EntityDamageEvent.DamageCause.FALL));
    }

    @EventHandler
    public void onWeatherChanger(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){ event.setCancelled(true); }

    @EventHandler
    public void onFlow(EntityChangeBlockEvent event){ event.setCancelled(true); }

    @EventHandler
    public void onAchievement(PlayerAchievementAwardedEvent event){ event.setCancelled(true); }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event){
        if(event.getNewGameMode().equals(GameMode.CREATIVE)){
            event.getPlayer().sendMessage(ChatColor.RED + "Attention tu peux maintenant casser/poser des blocs !");
        }
    }
}
