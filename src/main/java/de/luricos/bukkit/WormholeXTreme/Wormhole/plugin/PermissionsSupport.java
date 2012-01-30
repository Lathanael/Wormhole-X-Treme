/*
 * Wormhole X-Treme Plugin for Bukkit
 * Copyright (C) 2011 Lycano <https://github.com/lycano/Wormhole-X-Treme/>
 *
 * Copyright (C) 2011 Ben Echols
 *                    Dean Bailey
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

import de.luricos.bukkit.WormholeXTreme.Wormhole.WormholeXTreme;
import de.luricos.bukkit.WormholeXTreme.Wormhole.config.ConfigManager;
import de.luricos.bukkit.WormholeXTreme.Wormhole.utils.WXTLogger;

import org.bukkit.plugin.Plugin;

import com.nijikokun.bukkit.Permissions.Permissions;

import java.util.logging.Level;

/**
 * The Class PermissionsSupport.
 * 
 * @author alron
 */
public class PermissionsSupport {

    /**
     * Check permissions version.
     * 
     * @param version
     *            the version
     */
    private static void checkPermissionsVersion(final String version) {
        if (!version.startsWith("2.5") && !version.startsWith("2.6") && !version.startsWith("2.7") && !version.startsWith("3.0")) {
            WXTLogger.prettyLog(Level.WARNING, false, "Not a supported version of Permissions. Recommended is 3.0.x");
        }
    }

    /**
     * Disable permissions.
     */
    public static void disablePermissions() {
        if (WormholeXTreme.getPermissions() != null) {
            WormholeXTreme.setPermissions(null);
            WXTLogger.prettyLog(Level.INFO, false, "Detached from Permissions plugin.");
        }
    }

    /**
     * Setup permissions.
     */
    public static void enablePermissions() {
        if (!ConfigManager.getPermissionsSupportDisable()) {
            if (WormholeXTreme.getPermissions() == null) {
                final Plugin test = WormholeXTreme.getThisPlugin().getServer().getPluginManager().getPlugin("Permissions");
                if (test != null) {
                    final String v = test.getDescription().getVersion();
                    checkPermissionsVersion(v);
                    try {
                        WormholeXTreme.setPermissions(((Permissions) test).getHandler());
                        WXTLogger.prettyLog(Level.INFO, false, "Attached to Permissions version " + v);
                        if (ConfigManager.getSimplePermissions()) {
                            WXTLogger.prettyLog(Level.INFO, false, "Simple Permissions Enabled");
                        } else {
                            WXTLogger.prettyLog(Level.INFO, false, "Complex Permissions Enabled");
                        }
                    } catch (final ClassCastException e) {
                        WXTLogger.prettyLog(Level.WARNING, false, "Failed to get Permissions Handler. Defaulting to built-in permissions.");
                    }
                } else {
                    WXTLogger.prettyLog(Level.INFO, false, "Permission Plugin not yet available. Defaulting to built-in permissions until Permissions is loaded.");
                }
            }
        } else {
            WXTLogger.prettyLog(Level.INFO, false, "Permission Plugin support disabled via settings.txt.");
        }
    }
}
