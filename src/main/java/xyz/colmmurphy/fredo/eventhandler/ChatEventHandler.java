package xyz.colmmurphy.fredo.eventhandler;

import com.google.gson.Gson;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

public class ChatEventHandler extends FredoEventHandler{
    private Player player;
    @SubscribeEvent
    public void onChatEvent(ServerChatEvent event) {
        player = event.getPlayer();
        final String playerName = event.getUsername();
        final String messageContent = event.getMessage();

        String avatarUrl = getAvatarUrl();

        discordClient.sendWebhookMessage(playerName, avatarUrl, messageContent);
    }

    // this method is a fucking mess
    // taken mostly from https://www.baeldung.com/java-http-request and
    // https://github.com/colmmurphyxyz/minecraft-discord-link/blob/main/src/main/kotlin/xyz/colmmurphy/d2mc/D2MC.kt#L67
    private String getAvatarUrl() {
        try {
            Gson gson = new Gson();
            String UUID = player.getStringUUID();
            URL url = new URL(MessageFormat.format(
                    "https://sessionserver.mojang.com/session/minecraft/profile/{0}?unsigned=false", UUID));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            String json = content.toString();
            Map<String, String> map = gson.fromJson(json, Map.class);
            String texture = map.get("value");
            return MessageFormat.format("https://crafatar.com/avatars/{0}.png?size=128&overlay#{1}", UUID, texture);
        } catch (IOException e) {
            LOGGER.warn("something went wrong '_'");
            return "https://discordapp.com/assets/1cbd08c76f8af6dddce02c5138971129.png";
        }
    }
}
