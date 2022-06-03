package xyz.colmmurphy.fredo.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.minecraft.world.entity.player.Player;
import xyz.colmmurphy.fredo.Fredo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

enum FredoCommand {
    ONLINE("online", "view all online players", new Object[][]{}, (event) -> {
        String[] playerNames = (String[]) Fredo.onlinePlayers.stream().map(Player::getName).toArray();
        String online = Arrays.stream(playerNames).reduce("", (prev, s) -> prev + "\n" + s);
        event.reply(online)
                .setEphemeral(false)
                .queue();
        return;
    }),

    DEATHS("deaths", "see how many times a player has dies", new Object[][] {{OptionType.STRING, "playerName", "player's username", true}}, (event) -> {
        String playerName = event.getOption("playerName").getAsString();
        int deaths = Fredo.deathCounter.getOrZero(playerName);
        event.reply(playerName + " has died " + String.valueOf(deaths) + " times")
                .setEphemeral(true)
                .queue();
        }
    );
    final String name;
    final String description;
    private final Consumer<SlashCommandInteractionEvent> action;

    final Object[][] options;

    FredoCommand(String name, String description, Object[][] options, Consumer<SlashCommandInteractionEvent> action) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.options = options;
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
