package net.trendydevelopment.reportplugin.commands;

import net.trendydevelopment.reportplugin.Report;
import net.trendydevelopment.reportplugin.managers.ConfigManager;
import net.trendydevelopment.reportplugin.managers.MessageEnum;
import net.trendydevelopment.reportplugin.utils.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ReportCommand implements CommandExecutor {
    Report report = JavaPlugin.getPlugin(Report.class);
    ConfigManager configManager = report.getConfigManager();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.openInventory(configManager.getSelectionGui());
            return false;
        }
        if (Bukkit.getPlayerExact(args[0]) != null) {
            configManager.openReasonGui(p, Bukkit.getPlayer(args[0]));
        }
        if (Bukkit.getOfflinePlayer(args[0]) != null) {
            if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                p.sendMessage(MessageEnum.INVALID_PLAYER.getMessage());
                return false;
            }
            configManager.openReasonGui(p, Bukkit.getOfflinePlayer(args[0]));
            return false;
        }
        p.getPlayer().sendMessage(MessageEnum.INVALID_PLAYER.getMessage());
        return false;
    }
}
