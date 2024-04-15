package net.trendydevelopment.reportplugin.utils;



import net.trendydevelopment.reportplugin.Report;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTUtils {
    static Report plugin = JavaPlugin.getPlugin(Report.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void setNBT(ItemStack item, String key, Object value) {
        if(item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataType dataType;
            NamespacedKey Namedkey = new NamespacedKey(plugin, key);
            if(value instanceof String) {
                dataType = PersistentDataType.STRING;
                meta.getPersistentDataContainer().set(Namedkey, dataType, value);
            }
            if(value instanceof Double) {
                dataType = PersistentDataType.DOUBLE;
                meta.getPersistentDataContainer().set(Namedkey, dataType, value);
            }
            if(value instanceof Integer) {
                dataType = PersistentDataType.INTEGER;
                meta.getPersistentDataContainer().set(Namedkey, dataType, value);
            }
            item.setItemMeta(meta);
        }
    }

    public static boolean hasNBT(ItemStack item, String key) {
        NamespacedKey namedKey = new NamespacedKey(plugin, key);
        if(item != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if(container.has(namedKey , PersistentDataType.DOUBLE)) {
                return true;
            }
            if(container.has(namedKey , PersistentDataType.INTEGER)) {
                return true;
            }
            if(container.has(namedKey , PersistentDataType.STRING)) {
                return true;
            }
        }
        return false;
    }

    public static Object getNBT(ItemStack item, String key) {
        Object foundValue = null;
        NamespacedKey namedKey = new NamespacedKey(plugin, key);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.has(namedKey , PersistentDataType.DOUBLE)) {
            double value = container.get(namedKey, PersistentDataType.DOUBLE);
            foundValue = value;
        }
        if(container.has(namedKey , PersistentDataType.INTEGER)) {
            Integer value = container.get(namedKey, PersistentDataType.INTEGER);
            foundValue = value;
        }
        if(container.has(namedKey , PersistentDataType.STRING)) {
            String value = container.get(namedKey, PersistentDataType.STRING);
            foundValue = value;
        }
        return foundValue;
    }

}
