package net.runelite.client.plugins.projectilecd;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "<html><font color=#b82584>[J] Projectile Ticks",
	description = "Overlays a tick counter for projectiles",
	tags = {"projectile"},
	enabledByDefault = false
)
@Slf4j
public class ProjectileCounterPlugin extends Plugin
{
	private static final Splitter SPLITTER = Splitter.on("\n").omitEmptyStrings().trimResults();
	private static final String CONFIG_GROUP = "Projectile";
	private static final int MIN_COLOR_NUMBER = 1;
	private static final int MAX_COLOR_NUMBER = 3;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ProjectileCounterOverlay overlay;

	@Inject
	private ProjectileCounterConfig config;

	final Map<Projectile, ProjectileInfo> projectileInfos = new HashMap<>();
	private final Map<Integer, Color> projectileRules = new HashMap<>();

	@Provides
	ProjectileCounterConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProjectileCounterConfig.class);
	}

	@Override
	protected void startUp()
	{
		reset();
		rebuildProjectileRules();
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		reset();
		projectileRules.clear();
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (CONFIG_GROUP.equals(event.getGroup()))
		{
			rebuildProjectileRules();
			refreshTrackedProjectiles();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			reset();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Iterator<Map.Entry<Projectile, ProjectileInfo>> iterator = projectileInfos.entrySet().iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().getKey().getRemainingCycles() <= 0)
			{
				iterator.remove();
			}
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();
		Actor target = projectile.getInteracting();
		Player localPlayer = client.getLocalPlayer();
		if (config.showSelf() && target != null && !target.equals(localPlayer))
		{
			return;
		}

		Color color = projectileRules.get(projectile.getId());
		if (color != null)
		{
			projectileInfos.putIfAbsent(projectile, new ProjectileInfo(projectile, color));
		}
	}

	private void rebuildProjectileRules()
	{
		projectileRules.clear();
		for (String entry : SPLITTER.splitToList(config.allProjectile()))
		{
			String[] fields = entry.split(",", -1);
			if (fields.length != 2)
			{
				log.warn("Ignoring malformed projectile configuration: {}", entry);
				continue;
			}

			try
			{
				int projectileId = Integer.parseInt(fields[0].trim());
				int colorNumber = Integer.parseInt(fields[1].trim());
				if (colorNumber < MIN_COLOR_NUMBER || colorNumber > MAX_COLOR_NUMBER)
				{
					log.warn("Ignoring projectile configuration with invalid color number: {}", entry);
					continue;
				}

				projectileRules.put(projectileId, getConfiguredColor(colorNumber));
			}
			catch (NumberFormatException ex)
			{
				log.warn("Ignoring invalid projectile configuration: {}", entry);
			}
		}
	}

	private void refreshTrackedProjectiles()
	{
		Iterator<Map.Entry<Projectile, ProjectileInfo>> iterator = projectileInfos.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<Projectile, ProjectileInfo> entry = iterator.next();
			Color color = projectileRules.get(entry.getKey().getId());
			if (color == null)
			{
				iterator.remove();
			}
			else
			{
				entry.getValue().color = color;
			}
		}
	}

	private Color getConfiguredColor(int colorNumber)
	{
		switch (colorNumber)
		{
			case 2:
				return config.projColor2();
			case 3:
				return config.projColor3();
			default:
				return config.projColor();
		}
	}

	private void reset()
	{
		projectileInfos.clear();
	}
}
