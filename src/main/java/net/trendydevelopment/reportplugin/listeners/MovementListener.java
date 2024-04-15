package net.trendydevelopment.reportplugin.listeners;

import net.trendydevelopment.reportplugin.Report;
import net.trendydevelopment.reportplugin.managers.MessageEnum;
import net.trendydevelopment.reportplugin.managers.PlayerMessageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MovementListener implements Listener {
    PlayerMessageManager playerMessageManager = JavaPlugin.getPlugin(Report.class).getPlayerMessageManager();
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (playerMessageManager.getPlayerMessage().contains(e.getPlayer().getUniqueId())|| playerMessageManager.getReasonMessage().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            String[] args = MessageEnum.MOVE_TITLE.getMessage().split(",");
            e.getPlayer().sendTitle(args[0], args[1], 1, 20, 1);
        }
    }
}
