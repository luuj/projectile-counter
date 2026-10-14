package net.runelite.client.plugins.projectilecd;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;
import java.util.Iterator;

import static net.runelite.client.ui.overlay.OverlayUtil.renderTextLocation;

public class ProjectileCounterOverlay extends OverlayPanel {
    private final Client client;
    private final ProjectileCounterPlugin plugin;
    private final ProjectileCounterConfig config;
    private Font cachedFont;
    private int cachedFontSize;

    @Inject
    private ProjectileCounterOverlay(Client client, ProjectileCounterPlugin plugin, ProjectileCounterConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Iterator<ProjectileInfo> projectileIterator = this.plugin.projectileInfos.values().iterator();

        while (projectileIterator.hasNext()) {
            ProjectileInfo projectileInfo = projectileIterator.next();
            String textOverlay = Integer.toString(projectileInfo.currProjectile.getRemainingCycles()/30);
            LocalPoint localPoint = new LocalPoint((int) projectileInfo.currProjectile.getX(), (int) projectileInfo.currProjectile.getY());

            Point textLoc = Perspective.localToCanvas(client, localPoint, 0, Perspective.getTileHeight(client, localPoint, projectileInfo.currProjectile.getFloor()) - (int) projectileInfo.currProjectile.getZ());
            if (textLoc != null) {
                Point pointShadow = new Point(textLoc.getX() + 1, textLoc.getY() + 1);
                Font oldFont = graphics.getFont();
                graphics.setFont(getFont());
                renderTextLocation(graphics, pointShadow, textOverlay, Color.BLACK);
                renderTextLocation(graphics, textLoc, textOverlay, projectileInfo.color);
                graphics.setFont(oldFont);
            }
        }

        return super.render(graphics);
    }

    private Font getFont() {
        int textSize = config.textSize();
        if (cachedFont == null || cachedFontSize != textSize) {
            cachedFont = new Font("Arial", Font.PLAIN, textSize);
            cachedFontSize = textSize;
        }

        return cachedFont;
    }
}
