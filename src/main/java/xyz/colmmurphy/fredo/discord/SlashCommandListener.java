package xyz.colmmurphy.fredo.discord;

import com.ibm.icu.text.MessageFormat;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends DiscordEventListener {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        FredoCommand command = FredoCommand.fromName(e.getName());
        if (command == null) {
            LOGGER.warn(MessageFormat.format("Command {0} does not exist", e.getName()));
            return;
        }
        command.execute(e);
    }
}
