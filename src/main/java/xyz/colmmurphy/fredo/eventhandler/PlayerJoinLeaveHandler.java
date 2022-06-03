package xyz.colmmurphy.fredo.eventhandler;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.colmmurphy.fredo.Fredo;

public class PlayerJoinLeaveHandler extends FredoEventHandler {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Fredo.onlinePlayers.add(event.getPlayer());
        LOGGER.info(String.format("Added %s to the list of online players",
                event.getPlayer().getName()));
        discordClient.sendAnnouncement(event.getPlayer().getName().getString() + " joined the game");
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Fredo.onlinePlayers.remove(event.getPlayer());
        LOGGER.info(String.format("Removed %s from the list of online players",
                event.getPlayer().getName()));
        discordClient.sendAnnouncement("**" + event.getPlayer().getName().getString() + "** left the game");
    }
}
