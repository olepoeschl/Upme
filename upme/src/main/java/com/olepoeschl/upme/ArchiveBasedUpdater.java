package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.function.Consumer;

// TODO: javadoc
@NullMarked
public class ArchiveBasedUpdater implements Updater {

    private final String currentVersionString;
    private final UpdateResolver resolver;
    private final UpdateDownloader downloader;
    private final ArchiveTools archiveTools;

    private Map<String, UpdateStrategy> preparedUpdates;

    public ArchiveBasedUpdater(String currentVersionString, UpdateResolver resolver, UpdateDownloader downloader, ArchiveTools archiveTools) {
        this.currentVersionString = currentVersionString;
        this.resolver = resolver;
        this.downloader = downloader;
        this.archiveTools = archiveTools;
    }

    @Override
    public Version[] checkAvailableUpdates() throws IOException {
        return resolver.checkAvailableUpdates(currentVersionString);
    }

    @Override
    public void prepareUpdate(Version version, Consumer<Float> progressCallback) throws IOException {
        try {
            Path updateArchive = downloader.downloadUpdate(version.downloadUrl(), downloadProgress -> progressCallback.accept(downloadProgress * 0.5f));

            if(version.checksum() != null) { // if checksum is given, verify it
                try {
                    String calculatedChecksum = archiveTools.calculateChecksumOfFile(updateArchive, MessageDigest.getInstance("SHA-256"));
                    if (!calculatedChecksum.equals(version.checksum()))
                        throw new SecurityException("Checksum of downloaded file does not match expected checksum.");
                } catch (IOException e) {
                    throw new IOException("Could not compute checksum of downloaded archive file '" + updateArchive.getFileName() + "': " + e.getMessage(), e);
                } catch (NoSuchAlgorithmException e) {
                    // will never happen, because SHA-256 is supported on all platforms and MessageDigest implementations
                    throw new RuntimeException(e);
                }
            }

            Path updateDir = archiveTools.unpack(updateArchive, unpackProgress -> progressCallback.accept(unpackProgress * 0.5f + 0.5f));
            // TODO: where is the update strategy specified? this method is responsible for reading and verifying it
            var restartCommand = ""; // TODO
            var updateStrategy = new UpdateStrategy(updateDir, restartCommand);
            preparedUpdates.put(version.versionString(), updateStrategy);
        } catch (IOException e) {
            throw new IOException("Could not prepare update for version " + version.versionString() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Process applyUpdateAsync(Version version) {
        // TODO
        return null;
    }

    // TODO: is there anything else needed here?
    private record UpdateStrategy(Path updateDir, String restartCommand){}

}
