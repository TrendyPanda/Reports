package net.trendydevelopment.reportplugin;

import net.trendydevelopment.reportplugin.commands.ReportCommand;
import net.trendydevelopment.reportplugin.listeners.InventoryListener;
import net.trendydevelopment.reportplugin.listeners.MovementListener;
import net.trendydevelopment.reportplugin.listeners.PlayerMessageListener;
import net.trendydevelopment.reportplugin.managers.ConfigManager;
import net.trendydevelopment.reportplugin.managers.MessageManager;
import net.trendydevelopment.reportplugin.managers.PlayerMessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Report extends JavaPlugin {
    ConfigManager configManager;
    PlayerMessageManager playerMessageManager;
    MessageManager messageManager;
    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        playerMessageManager = new PlayerMessageManager(this);
        registerCommands();
        registerListeners();

    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMessageListener(), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(), this);
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    public void registerCommands() {
        getCommand("report").setExecutor(new ReportCommand());
    }

    public PlayerMessageManager getPlayerMessageManager() {
        return this.playerMessageManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
