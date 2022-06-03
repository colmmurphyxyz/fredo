package xyz.colmmurphy.fredo.discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import xyz.colmmurphy.fredo.Fredo;

public class GuildMessageListener extends DiscordEventListener {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.isWebhookMessage() || e.getAuthor().isBot()) return;
        if (!e.getChannel().getId().equals(System.getenv("CHANNEL_ID"))) return;

        Member member = e.getMember();
        assert member != null;
        MutableComponent messageFormatted = new TextComponent("<Discord> ")
                .withStyle(ChatFormatting.BLUE)
                .append(new TextComponent("<" + member.getEffectiveName() + "> ")
                        .withStyle(ChatFormatting.GOLD))
                .append(new TextComponent(e.getMessage().getContentStripped()))
                .withStyle(ChatFormatting.WHITE);
        Fredo.onlinePlayers.parallelStream().forEach( (player) ->
                player.sendMessage(messageFormatted, Util.NIL_UUID)
        );
    }
}
