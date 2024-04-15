package net.trendydevelopment.reportplugin.managers;

import net.trendydevelopment.reportplugin.Report;
import net.trendydevelopment.reportplugin.objects.PlayerReportObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerMessageManager {
    Report report;
    HashMap<UUID, PlayerReportObject> reasonMessage;
    List<UUID> playerMessage;
    public PlayerMessageManager(Report report) {
        this.report = report;
        playerMessage = new ArrayList<>();
        reasonMessage = new HashMap<>();
    }
    public void addPlayer(UUID uuid) {
        playerMessage.add(uuid);
    }
    public void addReason(UUID uuid, PlayerReportObject reason) {
        reasonMessage.put(uuid, reason);
    }

    public List<UUID> getPlayerMessage() {
        return playerMessage;
    }

    public HashMap<UUID, PlayerReportObject> getReasonMessage() {
        return reasonMessage;
    }
}
