package net.trendydevelopment.reportplugin.managers;

import net.trendydevelopment.reportplugin.Report;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public enum MessageEnum {

    TYPE_PLAYER {
        public String getMessage() {
            return colour("type-player");
        }
    }, REPORT_CANCELLED {
        public String getMessage() {
            return colour("report-cancelled");
        }
    }, INVALID_PLAYER {
        public String getMessage() {
            return colour("invalid-player");
        }
    }, MOVE_TITLE {
        public String getMessage() {
            return colour("move-title");
        }
    }, REPORT_SUCCESS {
        public String getMessage() {
            return colour("report-success");
        }
    }, TYPE_REASON {
        public String getMessage() {
            return colour("type-reason");
        }
    };

    public Report report = JavaPlugin.getPlugin(Report.class);
    public MessageManager messageManager = report.getMessageManager();

    public String colour(String str) {
        return report.getConfigManager().c((String) messageManager.getMessageMap().get(str));
    }

    public abstract String getMessage();
}
