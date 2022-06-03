package xyz.colmmurphy.fredo.eventhandler;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xyz.colmmurphy.fredo.Fredo;

public class PlayerDeathHandler extends FredoEventHandler {
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        String deathMessage = event.getSource().getLocalizedDeathMessage(event.getEntityLiving()).getString();
        String playerName = event.getEntityLiving().getName().getString();
        int playerDeaths = Fredo.deathCounter.getOrDefault(playerName, 0);
        Fredo.deathCounter.put(playerName, playerDeaths + 1);
        discordClient.sendDeathMessage(deathMessage);
    }
}
