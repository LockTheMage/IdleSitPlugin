package LockTheMage.idlesit;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.ClientTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.time.Duration;
import java.time.Instant;
import net.runelite.api.coords.WorldPoint;


@Slf4j
@PluginDescriptor(
        name = "Idle Sit",
        description = "Detects if the player is idle for a configured amount of time then sits",
        tags = {"idle", "detection"}
)
public class IdleSitPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private IdleSitConfig config;

    private Instant lastAnimating = Instant.now();
    private boolean isIdling = false;
    private WorldPoint lastLocation = null;

    @Provides
    IdleSitConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(IdleSitConfig.class);
    }

    @Subscribe
    public void onClientTick(ClientTick tick)
    {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return;
        }

        WorldPoint currentLocation = localPlayer.getWorldLocation();
        int pose = localPlayer.getPoseAnimation();
        int idlePose = localPlayer.getIdlePoseAnimation();
        int animation = localPlayer.getAnimation();
        int configAnimationId = 10083;


        if (lastLocation != null && !currentLocation.equals(lastLocation)) {
            if (isIdling) {
                localPlayer.setAnimation(-1);
            }
            lastAnimating = Instant.now();
            isIdling = false;
            lastLocation = currentLocation;
            return;
        }

        if ((animation != -1 && animation != configAnimationId) || pose != idlePose) {
            if (isIdling) {
                localPlayer.setAnimation(-1);
            }
            lastAnimating = Instant.now();
            isIdling = false;
            lastLocation = currentLocation;
            return;
        }

        final Duration idleDelay = Duration.ofSeconds(config.idleTimeout());
        if (!isIdling && Instant.now().isAfter(lastAnimating.plus(idleDelay))) {
            isIdling = true;
            localPlayer.setAnimation(configAnimationId);
            localPlayer.setAnimationFrame(0);
        }

        lastLocation = currentLocation;
    }
}
