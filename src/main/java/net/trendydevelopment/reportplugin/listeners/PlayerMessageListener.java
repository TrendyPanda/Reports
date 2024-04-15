package net.trendydevelopment.reportplugin.listeners;

import net.trendydevelopment.reportplugin.Report;
import net.trendydevelopment.reportplugin.managers.ConfigManager;
import net.trendydevelopment.reportplugin.managers.MessageEnum;
import net.trendydevelopment.reportplugin.managers.PlayerMessageManager;
import net.trendydevelopment.reportplugin.objects.PlayerReportObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.ietf.jgss.MessageProp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PlayerMessageListener implements Listener {
    Report report = JavaPlugin.getPlugin(Report.class);
    ConfigManager configManager = report.getConfigManager();
    PlayerMessageManager playerMessageManager = report.getPlayerMessageManager();

    @EventHandler
    public void onMessage(PlayerChatEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (playerMessageManager.getPlayerMessage().contains(uuid)) {
            e.setCancelled(true);
            if (e.getMessage().equalsIgnoreCase("cancel")) {
                playerMessageManager.getPlayerMessage().remove(uuid);
                e.getPlayer().sendMessage(MessageEnum.REPORT_CANCELLED.getMessage());
                return;
            }
            if (Bukkit.getPlayerExact(e.getMessage()) != null) {
                playerMessageManager.getPlayerMessage().remove(uuid);
                configManager.openReasonGui(e.getPlayer(), Bukkit.getPlayer(e.getMessage()));
                return;
            }
            if (Bukkit.getOfflinePlayer(e.getMessage()) != null) {
                if (!Bukkit.getOfflinePlayer(e.getMessage()).hasPlayedBefore()) {
                    playerMessageManager.getPlayerMessage().remove(uuid);
                    e.getPlayer().sendMessage(MessageEnum.INVALID_PLAYER.getMessage());
                    return;
                }
                configManager.openReasonGui(e.getPlayer(), Bukkit.getOfflinePlayer(e.getMessage()));
            }
            playerMessageManager.getPlayerMessage().remove(uuid);
            e.getPlayer().sendMessage(MessageEnum.INVALID_PLAYER.getMessage());

            return;
        }
        if (playerMessageManager.getReasonMessage().containsKey(uuid)) {
            e.setCancelled(true);
            if (e.getMessage().equalsIgnoreCase("cancel")) {
                playerMessageManager.getReasonMessage().remove(uuid);
                e.getPlayer().sendMessage(MessageEnum.REPORT_CANCELLED.getMessage());
                return;
            }
            PlayerReportObject playerReportObject = playerMessageManager.getReasonMessage().get(uuid);
            e.getPlayer().sendMessage(MessageEnum.REPORT_SUCCESS.getMessage().replace("%player%", playerReportObject.getReportName()));
            sendChatFilterWebhook(e.getPlayer(), playerReportObject, e.getMessage());
            playerMessageManager.getReasonMessage().remove(uuid);
        }
    }

    public void sendChatFilterWebhook(Player player, PlayerReportObject playerReportObject, String reason) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String webhookURL = configManager.getWebhookURL();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String date = LocalDateTime.now().format(formatter);

                String message = null;
                String embedTitle = "ᴘʟᴀʏᴇʀ ʀᴇᴘᴏʀᴛ";
                String embedDescription =
                                "\\n" +
                                "ᴘʟᴀʏᴇʀ: " + playerReportObject.getReportName() + "  \\n" +
                                "ʀᴇᴀꜱᴏɴ: " + playerReportObject.getReason() + "  \\n" +
                                "ᴇxᴘʟᴀɴᴀᴛɪᴏɴ: " + reason + "  \\n \\n" +
                                "ʀᴇᴘᴏʀᴛᴇᴅ ʙʏ: " + player.getName();

                String embedUrl = "https://mc-heads.net/head/" + playerReportObject.getReportName();
                HttpClient httpClient = HttpClient.newBuilder().build();
                String jsonPayload = String.format("{\"username\": \"%s\", \"avatar_url\": \"%s\",\"content\": %s, \"embeds\": [{\"title\": \"%s\", \"description\": \"%s\", \"color\": 16734296, \"footer\": {\"text\": \"%s\"}, \"thumbnail\": {\"url\": \"%s\"}}], \"attachments\": []}", "Ape", "https://i.postimg.cc/sfbT5Fd9/image.png", null, embedTitle, embedDescription, date, embedUrl);
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(webhookURL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                        .build();
                try {
                    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                } catch (Exception e) {
                    System.out.println("Failed to send webhook: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(report);
    }
}
