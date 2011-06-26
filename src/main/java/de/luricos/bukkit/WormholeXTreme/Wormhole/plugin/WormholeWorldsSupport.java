/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme>
 *
 * Copyright (C) 2011  Ben Echols
 *                     Dean Bailey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.luricos.bukkit.WormholeXTreme.Wormhole.plugin;

import de.luricos.bukkit.WormholeXTreme.Worlds.WormholeXTremeWorlds;
import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.model.StargateDBManager;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * The Class WormholeWorldsSupport.
 * 
 * @author alron
 */
public class WormholeWorldsSupport {

    /**
     * Check worlds version.
     * 
     * @param version
     *            the version
     * @return true, if successful
     */
    private static boolean checkWorldsVersion(final String version) {
        if (!version.startsWith("0.5")) {
            WormholeXTreme.getThisPlugin().prettyLog(Level.SEVERE, false, "Not a supported version of Wormhole Worlds. Recommended is 0.5");
            return false;
        }
        return true;
    }

    /**
     * Disable wormhole worlds.
     */
    public static void disableWormholeWorlds() {
        if (WormholeXTreme.getWorldHandler() != null) {
            WormholeXTreme.setWorldHandler(null);
            WormholeXTreme.getThisPlugin().prettyLog(Level.INFO, false, "Detached from Wormhole Worlds plugin.");
        }
    }

    /**
     * Enable wormhole worlds.
     */
    public static void enableWormholeWorlds() {
        if (ConfigManager.isWormholeWorldsSupportEnabled()) {
            if (WormholeXTreme.getWorldHandler() == null) {
                final Plugin worldsTest = WormholeXTreme.getThisPlugin().getServer().getPluginManager().getPlugin("WormholeXTremeWorlds");
                if (worldsTest != null) {
                    final String version = worldsTest.getDescription().getVersion();
                    if (checkWorldsVersion(version)) {
                        try {
                            WormholeXTreme.setWorldHandler(WormholeXTremeWorlds.getWorldHandler());
                            WormholeXTreme.getThisPlugin().prettyLog(Level.INFO, false, "Attached to Wormhole Worlds version " + version);
                            // Worlds support means we can continue our load.
                            StargateDBManager.loadStargates(WormholeXTreme.getThisPlugin().getServer());
                            WormholeXTreme.registerEvents(false);
                            WormholeXTreme.registerCommands();
                            WormholeXTreme.getThisPlugin().prettyLog(Level.INFO, true, "Enable Completed.");

                        } catch (final ClassCastException e) {
                            WormholeXTreme.getThisPlugin().prettyLog(Level.WARNING, false, "Failed to get cast to Wormhole Worlds: " + e.getMessage());
                        }
                    }
                } else {
                    WormholeXTreme.getThisPlugin().prettyLog(Level.INFO, false, "Wormhole Worlds Plugin not yet available Stargates will not load until it enables.");
                }
            }
        } else {
            WormholeXTreme.getThisPlugin().prettyLog(Level.INFO, false, "Wormhole X-Treme Worlds Plugin support disabled via settings.txt.");
        }
    }
}