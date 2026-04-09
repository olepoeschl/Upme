package com.olepoeschl.upme;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.function.Consumer;

/**
 * Provides methods for interacting with archive files.
 * Used by {@link ArchiveBasedUpdater} for checking the checksum of an archive file and unpacking it.
 *
 * @see ArchiveBasedUpdater
 * @since 1.0
 */
public class ArchiveTools {

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
    public Path unpack(Path pathToArchive, Consumer<Float> progressCallback) {
        if(!Files.isRegularFile(pathToArchive))
            throw new IllegalArgumentException("given path is not a regular file");

        // TODO
        return null;
    }

    // TODO: javadocs
    public String calculateChecksumOfFile(Path path, MessageDigest digest) throws IOException {
        if(!Files.isRegularFile(path))
            throw new IllegalArgumentException("given path is not a regular file");

        try (InputStream is = new BufferedInputStream(new FileInputStream(path.toFile()))) {

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            throw new IOException("Could not compute checksum: " + e.getMessage(), e);
        }

        byte[] bytes = digest.digest();
        StringBuilder checksumBuilder = new StringBuilder();

        for (byte b : bytes) {
            // TODO: check if this is correct
            checksumBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return checksumBuilder.toString();
    }

}
