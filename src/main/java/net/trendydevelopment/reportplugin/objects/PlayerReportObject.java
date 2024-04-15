package net.trendydevelopment.reportplugin.objects;

import java.util.UUID;

public class PlayerReportObject {
    UUID playerUUID;
    String reason;
    String reportName;
    String reportUUID;
    public PlayerReportObject(UUID playerUUID, String reason, String reportName, String reportUUID) {
        this.playerUUID = playerUUID;
        this.reason = reason;
        this.reportName = reportName;
        this.reportUUID = reportUUID;
    }

    public String getReason() {
        return reason;
    }

    public String getReportName() {
        return reportName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getReportUUID() {
        return reportUUID;
    }
}
