package com.olepoeschl.upme;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Interface defining methods for interacting with archive files.
 *
 * Used by {@link ArchiveBasedUpdater} for checking the checksum of an archive file and unpacking it.
 *
 * @see ArchiveBasedUpdater
 * @since 1.0
 */
public interface ArchiveTools {

    /**
     * Unpacks the archive located at the specified path to a temporary directory.
     *
     * @param pathToArchive the path to the archive file to unpack
     * @param progressCallback a callback function that receives progress updates as a float value, should be between
     *                         0.0 and 1.0
     * @return the path to the temporary directory the archive was unpacked to
     *
     * @since 1.0
     */
    Path unpack(Path pathToArchive, Consumer<Float> progressCallback);

    // TODO: javadocs
    String calculateChecksumOfFile(Path updateArchive);
}
