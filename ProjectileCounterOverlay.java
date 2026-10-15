package net.runelite.client.plugins.projectilecd;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class ProjectileCounterOverlay extends Overlay
{
	private static final int CYCLES_PER_TICK = 30;

	private final Client client;
	private final ProjectileCounterPlugin plugin;
	private final ProjectileCounterConfig config;

	@Inject
	private ProjectileCounterOverlay(Client client, ProjectileCounterPlugin plugin, ProjectileCounterConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(PRIORITY_HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.projectileInfos.isEmpty())
		{
			return null;
		}

		// Same font for every projectile, so set it once per frame.
		Font oldFont = graphics.getFont();
		graphics.setFont(config.font().getFont());

		for (ProjectileInfo projectileInfo : plugin.projectileInfos.values())
		{
			Projectile projectile = projectileInfo.currProjectile;
			int remainingCycles = projectile.getRemainingCycles();
			if (remainingCycles <= 0)
			{
				// Landed this frame; it's removed on the next game tick.
				continue;
			}

			// getX/getY/getZ are the projectile's current position in the scene, with Z as an
			// absolute height, so it can be projected directly without Projectile.getFloor().
			Point textLoc = Perspective.localToCanvas(client, (int) projectile.getX(), (int) projectile.getY(), (int) projectile.getZ());
			if (textLoc != null)
			{
				// OverlayUtil already draws a black 1px shadow under the text.
				OverlayUtil.renderTextLocation(graphics, textLoc, Integer.toString(remainingCycles / CYCLES_PER_TICK), projectileInfo.color);
			}
		}

		graphics.setFont(oldFont);
		return null;
	}
}
