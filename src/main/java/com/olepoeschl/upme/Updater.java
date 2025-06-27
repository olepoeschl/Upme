package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.Consumer;

@NullMarked
public interface Updater {

    /**
     * Checks for updates based on the current version and the update repository URL.
     *
     * @param currentVersion a semantic version string representing the current version of the application, e.g., "1.0.0"
     * @param updateRepositoryUrl the URL of the update repository where available versions are listed together with
     *                            their download links, and possibly additional metadata, e.g. version descriptions
     * @return a list of available versions to update to, where each update is represented by a {@link Version} object
     */
    List<Version> checkForUpdates(String currentVersion, String updateRepositoryUrl);

    /**
     * Downloads the update for the specified version and unpacks it if necessary. If the given {@code version} object
     * contains a non-null {@code checksumSha3256}, the downloaded file will also be verified against this checksum.
     *
     * @param version the version to prepare the update for
     * @param progressCallback a callback function that receives progress updates as a float value between 0.0 and 1.0
     */
    void prepareUpdate(Version version, Consumer<Float> progressCallback);

    /**
     * Starts the process that will asynchronously apply the update and returns its process handle. This method should
     * be called after {@link #prepareUpdate(Version, Consumer)} has completed successfully.
     *
     * <p>
     *     Note that it is required to exit the application after calling this method, as the application files cannot
     *     be replaced while the application is running.
     * </p>
     *
     * @param version the version to apply the update for
     * @return a {@link Process} object representing the ongoing update process, which can be used to monitor or control
     *         the update operation
     */
    Process applyUpdateAsync(Version version);
}
