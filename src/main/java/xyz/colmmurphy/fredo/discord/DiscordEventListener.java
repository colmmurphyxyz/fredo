package xyz.colmmurphy.fredo.discord;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class DiscordEventListener extends ListenerAdapter {
    final Logger LOGGER = LogManager.getLogger();
}
