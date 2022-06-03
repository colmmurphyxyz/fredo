package xyz.colmmurphy.fredo.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.minecraft.world.entity.player.Player;
import xyz.colmmurphy.fredo.Fredo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

enum FredoCommand {
    ONLINE("online", "view all online players", (event) -> {
        String[] playerNames = (String[]) Fredo.onlinePlayers.stream().map(Player::getName).toArray();
        String online = Arrays.stream(playerNames).reduce("", (prev, s) -> prev + "\n" + s);
        event.reply(online)
                .setEphemeral(false)
                .queue();
        return;
    });
    final String name;
    final String description;
    private final Consumer<SlashCommandInteractionEvent> action;

    FredoCommand(String name, String description, Consumer<SlashCommandInteractionEvent> action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public void execute(SlashCommandInteractionEvent e) {
        this.action.accept(e);
    }

    public static FredoCommand fromName(String commandName) {
        for (FredoCommand command : FredoCommand.values()) {
            if (command.name.equals(commandName)) {
                return command;
            }
        }
        return null;
    }
}
