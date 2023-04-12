package net.runelite.client.plugins.projectilecd;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Projectile;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.tickcd.NpcInfo;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@PluginDescriptor(
        name = "<html><font color=#b82584>[J] Projectile Ticks",
        description = "Overlays a tick counter for projectiles",
        tags = {"projectile"},
        enabledByDefault = false
)
public class ProjectileCounterPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ProjectileCounterOverlay overlay;
    @Inject
    private ProjectileCounterConfig config;
    private static final Splitter SPLITTER = Splitter.on("\n").omitEmptyStrings().trimResults();
    public ArrayList<ProjectileInfo> projectileList = new ArrayList();

    public ProjectileCounterPlugin() {
    }

    @Provides
    ProjectileCounterConfig getConfig(ConfigManager configManager) {
        return (ProjectileCounterConfig)configManager.getConfig(ProjectileCounterConfig.class);
    }

    protected void startUp() {
        this.reset();
        this.overlayManager.add(this.overlay);
    }

    protected void shutDown() {
        this.reset();
        this.overlayManager.remove(this.overlay);
    }

    private void reset() {
        this.projectileList.clear();
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        for(int i = this.projectileList.size() - 1; i >= 0; --i) {
            if (((ProjectileInfo)this.projectileList.get(i)).currProjectile.getRemainingCycles()/30 <= 0) {
                this.projectileList.remove(i);
            }
        }
    }

    @Subscribe
    public void onProjectileMoved(final ProjectileMoved event){
        final Projectile proj = event.getProjectile();
        Actor currTarget = proj.getInteracting();

        if (currTarget != null){
            if (config.showSelf() && !currTarget.equals(client.getLocalPlayer()))
                return;
        }

        List<String> strList = SPLITTER.splitToList(this.config.allProjectile());
        Iterator var4 = strList.iterator();

        while(var4.hasNext()) {
            String str = (String)var4.next();
            String[] stringList = str.split(",");
            if (stringList.length > 1){
                if (proj.getId() == Integer.valueOf(stringList[0])){
                    boolean contains = false;
                    for(int i = this.projectileList.size() - 1; i >= 0; --i) {
                        if (this.projectileList.get(i).currProjectile.getId() == proj.getId())
                        {
                            contains=true;
                            break;
                        }
                    }
                    if(!contains){
                        if(Integer.valueOf(stringList[1])==1) {
                            this.projectileList.add(new ProjectileInfo(proj, this.config.projColor()));
                        }else if(Integer.valueOf(stringList[1])==2){
                            this.projectileList.add(new ProjectileInfo(proj, this.config.projColor2()));
                        }
                        return;
                    }
                }
            }
        }
    }


}
