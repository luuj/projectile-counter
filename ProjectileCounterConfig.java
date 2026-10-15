package net.runelite.client.plugins.projectilecd;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.FontType;

@ConfigGroup("Projectile")
public interface ProjectileCounterConfig extends Config
{
	@ConfigSection(
		name = "Projectiles",
		description = "Which projectiles to count",
		position = 0
	)
	String projSettings = "projSettings";

	@ConfigSection(
		name = "Appearance",
		description = "How the overlay is drawn",
		position = 1
	)
	String appearanceSection = "appearanceSection";

	@ConfigItem(
		keyName = "allProjectile",
		name = "Projectile list",
		description = "One entry per line: projectile ID, color number (1-3). Example: 1234, 2",
		position = 0,
		section = projSettings
	)
	default String allProjectile()
	{
		return "";
	}

	@ConfigItem(
		keyName = "showSelf",
		name = "Hide other-targeted projectiles",
		description = "Hide projectiles aimed at another player or NPC. Projectiles aimed at the ground are still shown",
		position = 1,
		section = projSettings
	)
	default boolean showSelf()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		keyName = "projColor",
		name = "Tick color 1",
		description = "Text color for entries with color number 1",
		position = 0,
		section = appearanceSection
	)
	default Color projColor()
	{
		return Color.CYAN;
	}

	@Alpha
	@ConfigItem(
		keyName = "projColor2",
		name = "Tick color 2",
		description = "Text color for entries with color number 2",
		position = 1,
		section = appearanceSection
	)
	default Color projColor2()
	{
		return Color.GREEN;
	}

	@Alpha
	@ConfigItem(
		keyName = "projColor3",
		name = "Tick color 3",
		description = "Text color for entries with color number 3",
		position = 2,
		section = appearanceSection
	)
	default Color projColor3()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "font",
		name = "Font",
		description = "Font used for the overlay text",
		position = 3,
		section = appearanceSection
	)
	default FontType font()
	{
		return FontType.BOLD.withSize(20);
	}
}
