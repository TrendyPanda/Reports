package net.trendydevelopment.reportplugin.listeners;

import net.trendydevelopment.reportplugin.Report;
import net.trendydevelopment.reportplugin.managers.ConfigManager;
import net.trendydevelopment.reportplugin.managers.MessageEnum;
import net.trendydevelopment.reportplugin.managers.PlayerMessageManager;
import net.trendydevelopment.reportplugin.objects.PlayerReportObject;
import net.trendydevelopment.reportplugin.utils.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryListener implements Listener {
    Report report = JavaPlugin.getPlugin(Report.class);
    ConfigManager configManager = report.getConfigManager();
    PlayerMessageManager playerMessageManager = report.getPlayerMessageManager();
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(configManager.getReasonTitle())) {
            e.setCancelled(true);
            if (!NBTUtils.hasNBT(e.getCurrentItem(), "gui-type")) return;
            String nbt = (String)NBTUtils.getNBT(e.getCurrentItem(), "gui-type");
            if (nbt.equals("none")) return;
            if (nbt.equals("back")) {
                p.openInventory(configManager.getSelectionGui());
                return;
            }
            if (nbt.equals("playerhead")) return;
            String playerName = (String) NBTUtils.getNBT(e.getCurrentItem(),"playername");
            String playerUUID = (String) NBTUtils.getNBT(e.getCurrentItem(), "playeruui");
            playerMessageManager.addReason(p.getUniqueId(), new PlayerReportObject(p.getUniqueId(), nbt, playerName, playerUUID));
            p.sendMessage(MessageEnum.TYPE_REASON.getMessage());
            p.closeInventory();
            return;
        }
        if (e.getInventory().equals(configManager.getSelectionGui())){
            e.setCancelled(true);
            if (!NBTUtils.hasNBT(e.getCurrentItem(), "gui-type")) return;
            String nbt = (String)NBTUtils.getNBT(e.getCurrentItem(), "gui-type");
            if (nbt.equals("none")) return;
            if (nbt.equals("report")) {
                playerMessageManager.addPlayer(p.getUniqueId());
                p.sendMessage(MessageEnum.TYPE_PLAYER.getMessage());
                p.closeInventory();
            }
        }
    }
}
