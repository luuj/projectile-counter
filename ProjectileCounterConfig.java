package net.runelite.client.plugins.projectilecd;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("Projectile")
public interface ProjectileCounterConfig extends Config {
    @ConfigSection(
            name = "Projectiles",
            description = "Projectile counter settings",
            position = 0
    )
    String projSettings = "projSettings";
    @ConfigItem(
            name = "Projectile ID List",
            keyName = "allProjectile",
            description = "One entry per line: projectile ID, color number (1-3)",
            position = 1,
            section = "projSettings"
    )
    default String allProjectile() {
        return "";
    }

    @ConfigItem(
            keyName = "projColor",
            name = "Tick Number Color 1",
            description = "Color of projectile tick counter",
            position = 3,
            section = "projSettings"
    )
    default Color projColor()
    {
        return Color.CYAN;
    }

    @ConfigItem(
            keyName = "projColor2",
            name = "Tick Number Color 2",
            description = "Color of projectile tick counter",
            position = 2,
            section = "projSettings"
    )
    default Color projColor2()
    {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "projColor3",
            name = "Tick Number Color 3",
            description = "Color of projectile tick counter",
            position = 4,
            section = "projSettings"
    )
    default Color projColor3()
    {
        return Color.RED;
    }

    @Range(
            min = 5,
            max = 50
    )
    @ConfigItem(
            keyName = "textSize",
            name = "Text Size",
            description = "Sets the text size of the ticks overlay",
            position = 5,
            section = "projSettings"
    )
    default int textSize() {
        return 20;
    }

    @ConfigItem(
            keyName = "showSelf",
		name = "Hide other-targeted projectiles",
		description = "Hide projectiles explicitly targeted at another actor; ground-targeted projectiles are still shown",
            position = 6,
            section = "projSettings"
    )
    default boolean showSelf() {
        return true;
    }
}
