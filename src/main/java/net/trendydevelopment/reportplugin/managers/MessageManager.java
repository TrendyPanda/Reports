package net.trendydevelopment.reportplugin.managers;

import net.trendydevelopment.reportplugin.Report;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MessageManager {
    Report report;
    FileConfiguration config;
    Map<String, Object> messageMap;

    public MessageManager(Report report) {
        this.report = report;
        config = createMessages();
        messageMap = loadMessages();
    }
    private FileConfiguration createMessages() {
        File Messagefile = new File(report.getDataFolder(), "messages.yml");

        if (!Messagefile.exists()) {
            Messagefile.getParentFile().mkdirs();
            report.saveResource("messages.yml", false);
        }
        FileConfiguration message = new YamlConfiguration();
        try {
            message.load(Messagefile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return message;
    }
    private Map<String, Object> loadMessages() {
        return config.getConfigurationSection("messages").getValues(true);
    }

    public Map<String, Object> getMessageMap() {
        return messageMap;
    }
}

