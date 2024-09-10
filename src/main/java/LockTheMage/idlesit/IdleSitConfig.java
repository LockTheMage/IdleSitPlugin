package LockTheMage.idlesit;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("LockRestIdle")
public interface IdleSitConfig extends Config
{
    @ConfigItem(
            keyName = "idleTimeout",
            name = "Idle Time",
            description = "The time (in seconds) the player will sit and rest."
    )
    @Units(Units.SECONDS)
    @Range(min = 1, max = 20)
    default int idleTimeout()
    {
        return 3;
    }
}
