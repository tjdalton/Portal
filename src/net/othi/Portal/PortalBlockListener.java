package net.othi.Portal;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * Portal block listener
 * @author oTHi
 */
@SuppressWarnings("unused")
public class PortalBlockListener extends BlockListener {
    private final Portal plugin;

    public PortalBlockListener(final Portal plugin) {
        this.plugin = plugin;
    }

    //put all Block related code here
}
