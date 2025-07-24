package com.olepoeschl.upme;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * The Updater interface provides methods to perform the complete update process for an application.
 * It includes three steps:
 * <ol>
 *     <li>Checking for available updates</li>
 *     <li>Preparing the update, typically by downloading it and unpacking it if necessary</li>
 *     <li>Applying the update asynchronously, which typically involves starting a new process to replace the current
 *     application files</li>
 * </ol>
 *
 * @author Ole Pöschl
 * @version 1.0
 * @since 1.0
 */
public interface Updater {

    /**
     * Checks for updates based on the current version and the update repository URL.
     *
     * @return a list of available versions to update to, where each update is represented by a {@link Version} object
     * @see UpdateResolver#checkAvailableUpdates(String)
     * @since 1.0
     */
    Version[] checkAvailableUpdates() throws IOException;

    /**
     * Downloads the update for the specified version and unpacks it if necessary. If the given {@code version} object
     * contains a non-null {@code checksumSha3256}, the downloaded file will also be verified against this checksum.
     *
     * @param version the version to prepare the update for
     * @param progressCallback a callback function that receives progress updates as a float value between 0.0 and 1.0
     *
     * @see UpdateDownloader#downloadUpdate(String, Consumer)
     * @see ArchiveUnpacker#unpack(Path, Consumer)
     * @since 1.0
     */
    void prepareUpdate(Version version, Consumer<Float> progressCallback);

    /**
     * Starts the process that will asynchronously apply the update and returns its process handle. This method should
     * be called after {@link #prepareUpdate(Version, Consumer)} has completed successfully.
     * <p>
     *     Note that it is required to exit the application after calling this method, as the application files cannot
     *     be replaced while the application is running.
     * </p>
     *
     * @param version the version to apply the update for
     * @return a {@link Process} object representing the ongoing update process, which can be used to monitor or control
     *         the update operation
     *
     * @since 1.0
     */
    Process applyUpdateAsync(Version version);
}
