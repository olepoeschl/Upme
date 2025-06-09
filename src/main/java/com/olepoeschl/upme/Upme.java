package com.olepoeschl.upme;

/**
 * This class conveniently wraps the update functionality of the Upme library in a static interface, implementing the
 * simplest use case possible: check for an update and install it if available.
 * <p>
 *     Automatically chooses the correct {@link UpdateResolver} implementation based on the URL scheme, the correct
 *     {@link UpdateProvider} implementation based on the gathered update information, and the correct
 *     {@link UpdateApplier} implementation dependent on the downloaded update file.
 * <p>
 * Usage:
 * <ol>
 *     <li>{@link Upme#init}: Initialize with the current version and update server URL.</li>
 *     <li>{@link Upme#possible}: Check if an update is available.</li>
 *     <li>{@link Upme#now}: If available, download and install the update.</li>
 * </ol>
 */
public final class Upme {

    /**
     * Initializes the static Upme class with the current version and the update server URL.
     * @param currentVersion The current version of the application.
     * @param updateServerUrl The URL to check for updates.
     */
    public void init(String currentVersion, String updateServerUrl) {
        // TODO
    }

    /**
     * Checks if there is an update available.
     * @return true if an update is available, false otherwise.
     */
    public static boolean possible() {
        return false; // TODO
    }

    /**
     * If an update is available, this method will download and install it and restart the application.
     */
    public void now() {
        // TODO
    }

}
