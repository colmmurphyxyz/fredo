package xyz.colmmurphy.fredo.eventhandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.colmmurphy.fredo.discord.DiscordClient;

abstract class FredoEventHandler {
    final DiscordClient discordClient = DiscordClient.getInstance();
    final Logger LOGGER = LogManager.getLogger();
}
