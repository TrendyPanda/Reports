package net.trendydevelopment.reportplugin.managers;

import net.trendydevelopment.reportplugin.Report;
import net.trendydevelopment.reportplugin.utils.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {
    Report report;
    FileConfiguration config;
    ConfigurationSection selectionConfig;
    ConfigurationSection reasonConfig;
    Inventory selectionInventory;
    Inventory reasonInventory;
    String reasonTitle;
    String webhookURL;

    public ConfigManager(Report report) {
        this.report = report;
        this.config = createConfig();
        selectionConfig = config.getConfigurationSection("selection-gui");
        reasonConfig = config.getConfigurationSection("reason-gui");
        reasonTitle = c(reasonConfig.getString("title"));
        webhookURL = config.getString("webhook-URL");
        selectionInventory = createSelectionGUI();
        reasonInventory = createReasonGui();
    }

    private FileConfiguration createConfig() {
        File reportFile = new File(report.getDataFolder(), "report.yml");
        if (!reportFile.exists()) {
            reportFile.getParentFile().mkdirs();
            report.saveResource("report.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(reportFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    public Inventory createSelectionGUI() {
        Inventory inventory = Bukkit.createInventory(null, selectionConfig.getInt("size") * 9, c(selectionConfig.getString("title")));
        for (String str : selectionConfig.getConfigurationSection("items").getKeys(false)) {
            ConfigurationSection itemSel = selectionConfig.getConfigurationSection("items." + str);
            String guiType = itemSel.getString("gui-type");
            Material itemType = Material.getMaterial(itemSel.getString("item-type"));
            Integer itemSlot = itemSel.getInt("item-slot");
            String itemName = c(itemSel.getString("item-name"));
            List<String> lore = c(itemSel.getStringList("lore"));
            Boolean glowing = itemSel.getBoolean("glowing");
            ItemStack item = new ItemStack(itemType);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemMeta.setLore(lore);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
            NBTUtils.setNBT(item, "gui-type", guiType);
            if (glowing) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            inventory.setItem(itemSlot, item);
        }
        return inventory;
    }

    public Inventory createReasonGui() {
        Inventory inventory = Bukkit.createInventory(null, reasonConfig.getInt("size") * 9, reasonTitle);
        for (String str : reasonConfig.getConfigurationSection("items").getKeys(false)) {
            ConfigurationSection itemSel = reasonConfig.getConfigurationSection("items." + str);
            String guiType = itemSel.getString("gui-type");
            Material itemType = Material.getMaterial(itemSel.getString("item-type"));
            Integer itemSlot = itemSel.getInt("item-slot");
            String itemName = c(itemSel.getString("item-name"));
            List<String> lore = c(itemSel.getStringList("lore"));
            Boolean glowing = itemSel.getBoolean("glowing");
            ItemStack item = new ItemStack(itemType);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemMeta.setLore(lore);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(itemMeta);
            NBTUtils.setNBT(item, "gui-type", guiType);
            if (glowing) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            inventory.setItem(itemSlot, item);
        }
        return inventory;
    }

    public String c(String str) {
        return ChatColor.translateAlternateColorCodes('&', hex(str));
    }
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }
    public List<String> c(List<String> str) {
        List<String> stringList = new ArrayList<>();
        str.forEach(s -> {
            stringList.add(c(s));
        });
        return stringList;
    }

    public String getReasonTitle() {
        return reasonTitle;
    }

    public Inventory getSelectionGui() {
        return selectionInventory;
    }

    public Inventory getReasonGui() {
        return reasonInventory;
    }

    public void openReasonGui(Player p, OfflinePlayer player) {
        Inventory templateGUI = getReasonGui();
        Inventory reasonGUI = Bukkit.createInventory(null, templateGUI.getSize(), reasonTitle);
        reasonGUI.setContents(templateGUI.getContents());
        for (ItemStack item : reasonGUI.getContents()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;
            NBTUtils.setNBT(item, "playername", player.getName());
            NBTUtils.setNBT(item, "playeruuid", player.getUniqueId().toString());
            if (item.getType().equals(Material.PLAYER_HEAD)) {
                SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
                skullmeta.setOwningPlayer(player);
                item.setItemMeta(skullmeta);
            }
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(itemMeta.getDisplayName().replace("%player%", player.getName()));
            if (itemMeta.getLore() != null) {
                List<String> lore = new ArrayList<>();
                itemMeta.getLore().forEach(s -> {
                    lore.add(s.replace("%player%", player.getName()));
                });
                itemMeta.setLore(lore);
            }
            item.setItemMeta(itemMeta);
        }
        p.openInventory(reasonGUI);
    }

    public String getWebhookURL() {
        return webhookURL;
    }
}
