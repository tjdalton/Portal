package net.othi.Portal;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * 
 * @author oTHi
 * @email the.othi@gmail.com
 */
@SuppressWarnings("unused")
public class PortalPlayerListener extends PlayerListener {
	private final Portal plugin;

	public PortalPlayerListener(Portal instance) {
		plugin = instance;
	}

	// Portal layout
	int[][] matrix = { { 49, 49, 49, 49 }, { 49, 0, 0, 49 }, { 49, 0, 0, 49 },
			{ 49, 0, 0, 49 }, { 49, 49, 49, 49 } };

	public void onPlayerMove(PlayerMoveEvent event) {
		Location loc = event.getPlayer().getLocation();
		World world = null;
		double factor;
		// Check which world player is currently in, and set factor
		if (loc.getWorld().getEnvironment() == World.Environment.NORMAL) {
			for (int i = 0; i < plugin.getServer().getWorlds().size(); i++) {
				world = plugin.getServer().getWorlds().get(i);
				if (world.getEnvironment() == World.Environment.NETHER)
					break;
			}
			factor = 1/16;
		} else {
			for (int i = 0; i < plugin.getServer().getWorlds().size(); i++) {
				world = plugin.getServer().getWorlds().get(i);
				if (world.getEnvironment() == World.Environment.NORMAL)
					break;
			}
			factor = 16;
		}
		 factor = 1;
		// Check if player is standing on a Netherportal.
		if ((loc.getWorld().getBlockTypeIdAt(loc.getBlockX(),
				loc.getBlockY() - 1, loc.getBlockZ()) == Material.OBSIDIAN
				.getId())) {
			if ((loc.getWorld().getBlockTypeIdAt(loc.getBlockX(),
					loc.getBlockY(), loc.getBlockZ()) == Material.PORTAL
					.getId())) {
				Location newLoc = new Location(world, loc.getX() * factor,
						loc.getY(), loc.getZ() * factor, loc.getPitch(),
						loc.getYaw());
				//newLoc.setX(newLoc.getX()-2);
				//newLoc.setY(newLoc.getY()-2);
				if(!world.isChunkLoaded(newLoc.getBlockX(), newLoc.getBlockY())){
					world.loadChunk(newLoc.getBlockX(), newLoc.getBlockY());
				}
				// Check to see if we need to make a new connecting portal
				if (!(((newLoc.getWorld().getBlockTypeIdAt(newLoc.getBlockX(),
						newLoc.getBlockY() - 1, newLoc.getBlockZ()) == Material.OBSIDIAN
						.getId()) && (newLoc.getWorld().getBlockTypeIdAt(
						newLoc.getBlockX() - 1, newLoc.getBlockY() - 1,
						newLoc.getBlockZ()) == Material.OBSIDIAN.getId())) || ((newLoc
						.getWorld().getBlockTypeIdAt(newLoc.getBlockX() + 1,
								newLoc.getBlockY() - 1, newLoc.getBlockZ()) == Material.OBSIDIAN
						.getId())
						&& (newLoc.getWorld().getBlockTypeIdAt(
								newLoc.getBlockX(), newLoc.getBlockY() - 1,
								newLoc.getBlockZ()) == Material.OBSIDIAN
								.getId())
						|| ((newLoc.getWorld().getBlockTypeIdAt(
								newLoc.getBlockX(), newLoc.getBlockY() - 1,
								newLoc.getBlockZ() + 1) == Material.OBSIDIAN
								.getId()) && (newLoc.getWorld()
								.getBlockTypeIdAt(newLoc.getBlockX(),
										newLoc.getBlockY() - 1,
										newLoc.getBlockZ()) == Material.OBSIDIAN
								.getId())) || ((newLoc.getWorld()
						.getBlockTypeIdAt(newLoc.getBlockX(),
								newLoc.getBlockY() - 1, newLoc.getBlockZ() - 1) == Material.OBSIDIAN
						.getId()) && (newLoc.getWorld().getBlockTypeIdAt(
						newLoc.getBlockX(), newLoc.getBlockY() - 1,
						newLoc.getBlockZ()) == Material.OBSIDIAN.getId()))))) {
					System.out.println("Recycling Portal");
					int x = 0;
					// Try to avoid creating a portal in the air, move down if
					// so
					while (newLoc.getWorld().getBlockTypeIdAt(
							newLoc.getBlockX(), newLoc.getBlockY() - 1,
							newLoc.getBlockZ()) == Material.AIR.getId()
							|| newLoc.getWorld().getBlockTypeIdAt(
									newLoc.getBlockX(), newLoc.getBlockY() - 1,
									newLoc.getBlockZ()) == Material.OBSIDIAN
									.getId()
							|| newLoc.getWorld().getBlockTypeIdAt(
									newLoc.getBlockX(), newLoc.getBlockY() - 1,
									newLoc.getBlockZ()) == Material.PORTAL
									.getId()) {
						if (newLoc.getBlockY() >= 2) {
							newLoc.setY(newLoc.getY() - 1);
							x--;
						}
					}
					System.out.println("Adjusting Y Down: " + x);
					x = 0;
					// Try to avoid creating a portal in solid rock, move up if
					// so
					while (newLoc.getWorld().getBlockTypeIdAt(
							newLoc.getBlockX() + 1, newLoc.getBlockY(),
							newLoc.getBlockZ()) != Material.AIR.getId()
							&& newLoc.getWorld().getBlockTypeIdAt(
									newLoc.getBlockX() + 2, newLoc.getBlockY(),
									newLoc.getBlockZ()) != Material.AIR.getId()
							&& newLoc.getWorld().getBlockTypeIdAt(
									newLoc.getBlockX() + 3, newLoc.getBlockY(),
									newLoc.getBlockZ()) != Material.AIR.getId()
							&& newLoc.getWorld().getBlockTypeIdAt(
									newLoc.getBlockX() + 4, newLoc.getBlockY(),
									newLoc.getBlockZ()) != Material.AIR.getId()) {
						if (newLoc.getBlockY() <= 119) {
							newLoc.setY(newLoc.getY() + 1);
							x++;
						} else
							break;
					}
					System.out.println("Adjusting Y Up: " + x);
					// Make sure there is enough room to spawn
					for (int i = 0; i < 5; i++) {
						for (int j = 1; j < 3; j++) {
							// for (int k = -2; k < 2; k++) {
							world.getBlockAt(newLoc.getBlockX() + j,
									newLoc.getBlockY() + i, newLoc.getBlockZ())
									.setTypeId(Material.AIR.getId());
							world.getBlockAt(newLoc.getBlockX() + j,
									newLoc.getBlockY() + i,
									newLoc.getBlockZ() - 1).setTypeId(
									Material.AIR.getId());
							world.getBlockAt(newLoc.getBlockX() + j,
									newLoc.getBlockY() + i,
									newLoc.getBlockZ() + 1).setTypeId(
									Material.AIR.getId());
							// }
						}
					}

					// Clears out any remnant obsidian
					for (int i = -20; i <= 20; i++) {
						for (int j = -20; j <= 20; j++) {
							for (int k = -20; k <= 20; k++) {
								if (newLoc.getWorld().getBlockTypeIdAt(
										newLoc.getBlockX() + j,
										newLoc.getBlockY() + i,
										newLoc.getBlockZ() + k) == Material.OBSIDIAN
										.getId()
										|| newLoc.getWorld().getBlockTypeIdAt(
												newLoc.getBlockX() + j,
												newLoc.getBlockY() + i,
												newLoc.getBlockZ() + k) == Material.PORTAL
												.getId()) {
									world.getBlockAt(newLoc.getBlockX() + j,
											newLoc.getBlockY() + i,
											newLoc.getBlockZ() + k).setTypeId(
											Material.AIR.getId());
								}
							}
						}
					}
					// Generate the new portal
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 4; j++) {
							world.getBlockAt(newLoc.getBlockX() + j,
									newLoc.getBlockY() + i, newLoc.getBlockZ())
									.setTypeId(matrix[i][j]);
						}
					}
					// Get rid of any remnant portal blocks.
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 4; j++) {
							for (int k = 0; k < 5; k++) {
								if (newLoc.getWorld().getBlockTypeIdAt(
										newLoc.getBlockX() + j,
										newLoc.getBlockY() + i,
										newLoc.getBlockZ() + k) == Material.PORTAL
										.getId()) {
									world.getBlockAt(newLoc.getBlockX() + j,
											newLoc.getBlockY() + i,
											newLoc.getBlockZ() + k).setTypeId(
											Material.AIR.getId());
								}
							}
						}
					}

				}
				// Adjust destination to prevent clipping and trigger portal.
				newLoc.setX(newLoc.getBlockX() + 2);
				newLoc.setY(newLoc.getBlockY() + 2);
				// Update from/to to avoid 'player moved wrongly'
				event.setFrom(newLoc);
				event.setTo(newLoc);
				// System.out.println("Worlds: " +
				// newLoc.getWorld().getEnvironment().toString());
				event.getPlayer().teleportTo(newLoc);
				System.out.println(event.getPlayer().getDisplayName()
						+ " was transported to: "
						+ newLoc.getWorld().getEnvironment().toString());
			}
		}
	}

}
