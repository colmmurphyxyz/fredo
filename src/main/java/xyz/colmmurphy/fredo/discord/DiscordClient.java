package xyz.colmmurphy.fredo.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscordClient {

    private static final Logger LOGGER = LogManager.getLogger();
    private JDA jda;

    private final String guildID = System.getenv("GUILD_ID");
    private final String channelID = System.getenv("CHANNEL_ID");
    private Guild guild;
    private TextChannel channel;

    private Icon avatarIcon;
    private String avatarUrl;

    public WebhookClient webhookClient;

    private static DiscordClient INSTANCE;
    private DiscordClient() {
        String token = System.getenv("TOKEN");
        try {
            this.jda = JDABuilder.createLight(token,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_WEBHOOKS,
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .setActivity(Activity.playing("Ben's server"))
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS,
                            CacheFlag.ONLINE_STATUS)
                    .addEventListeners(new GuildMessageListener())
                    .build()
                    .awaitReady();
        } catch (LoginException | InterruptedException e) {
            LOGGER.warn("oopsie daisies :(");
        }

        // initialise all slash commands
        for (FredoCommand command : FredoCommand.values()) {
            jda.upsertCommand(command.name, command.description).queue();
        }
        LOGGER.info(String.format("Successfully logged into discord as %s#%s",
                jda.getSelfUser().getName(), jda.getSelfUser().getDiscriminator()));

        // connect to guild/channel
        this.guild = this.jda.getGuildById(guildID);
        this.channel = this.jda.getTextChannelById(channelID);
        if (this.guild == null || this.channel == null) {
            LOGGER.error("The bot could not find the guild and/or channel with the given ID, did you enter both ID's correctly, " +
                    "is the bot a member of the guild?");
        }

        avatarUrl = jda.getSelfUser().getAvatarUrl();
        try {
            assert avatarUrl != null;
            avatarIcon = Icon.from(new URL(avatarUrl).openStream());
        } catch (IOException e) { LOGGER.error("uh-oh"); }

        assert channel != null;
        channel.createWebhook("Fredo")
                .setAvatar(avatarIcon)
                .queue( (createdWebhook) -> {
                            webhookClient = WebhookClient.withUrl(createdWebhook.getUrl());
                            LOGGER.info("created webhook");
                            webhookClient.send(":green_circle: Server is online :green_circle:");
                        }
                );
    }

    public static DiscordClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DiscordClient();
        }
        return INSTANCE;
    }

    public void sendAnnouncement(String message) {
        channel.sendMessage(message).queue();
    }

    public void sendWebhookMessage(String playerName, String playerAvatarUrl, String message) {
        String timeOfMessage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(playerName)
                .setAvatarUrl(playerAvatarUrl)
                .setContent(String.format("**[%s]** %s",
                        timeOfMessage, message));
        webhookClient.send(builder.build());
    }

    public void sendWebhookAnnouncement(String message) {
        this.sendWebhookMessage("Fredo", avatarUrl, message);
    }

    public void sendDeathMessage(String deathMessage) {
        channel.sendMessage(deathMessage).queue( (msg) -> {
            msg.addReaction("U+1F1EB").queue();
        });
    }

    @Override
    public String toString() {
        return "TODO: implement this method";
    }
}
