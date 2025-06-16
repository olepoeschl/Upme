package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * Static entry point acting as a facade for the {@link UpdateManager}. Use when you simply want to update to the latest
 * available version.
 * <br>
 * Usage:
 * <ol>
 *     <li>(Optional) Call {@link Upme#setUpdateManager} to inject a custom UpdateManager instance.</li>
 *     <li>Call {@link Upme#initialize(String, String)} with the current version and the update server URL.</li>
 *     <li>Check if an update is available using {@link Upme#possible()}.</li>
 *     <li>If yes, call {@link Upme#now()} to download and apply the latest update.</li>
 * </ol>
 */
@NullMarked
public final class Upme {

    private static UpdateManager updateManager = new UpdateManager();

    /**
     * Sets a custom {@link UpdateManager} instance. This is optional; if not set, a default instance will be used.
     *
     * @param updateManager the custom UpdateManager instance to use
     */
    public static void setUpdateManager(UpdateManager updateManager) {
        Upme.updateManager = Objects.requireNonNull(updateManager, "updateManager must not be null");
    }

    /**
     * Gets the current {@link UpdateManager} instance.
     *
     * @return the current UpdateManager instance
     */
    public static UpdateManager getUpdateManager() {
        return updateManager;
    }

    /**
     * Initializes the update manager with the current version and the update server URL.
     *
     * @param currentVersion   the current version of the application
     * @param updateServerUrl  the URL of the update server
     */
    public static void initialize(String currentVersion, String updateServerUrl) {
        updateManager.initialize(currentVersion, updateServerUrl);
    }

    /**
     * Checks if an update is available.
     *
     * @return true if an update is available, false otherwise
     */
    public static boolean possible() {
        return updateManager.possible();
    }

    /**
     * Downloads and applies the latest update if available.
     * This method must be called after checking with {@link Upme#possible()}.
     */
    public static void now() {
        updateManager.now();
    }
}
