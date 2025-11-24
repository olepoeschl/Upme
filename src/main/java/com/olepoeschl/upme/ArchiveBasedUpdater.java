package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

// TODO: javadoc
@NullMarked
public class ArchiveBasedUpdater implements Updater {

    private final String currentVersionString;
    private final UpdateResolver resolver;
    private final UpdateDownloader downloader;
    private final ArchiveUnpacker unpacker;

    private Map<String, UpdateStrategy> preparedUpdates;

    public ArchiveBasedUpdater(String currentVersionString, UpdateResolver resolver, UpdateDownloader downloader, ArchiveUnpacker unpacker) {
        this.currentVersionString = currentVersionString;
        this.resolver = resolver;
        this.downloader = downloader;
        this.unpacker = unpacker;
    }

    @Override
    public Version[] checkAvailableUpdates() throws IOException {
        return resolver.checkAvailableUpdates(currentVersionString);
    }

    @Override
    public void prepareUpdate(Version version, Consumer<Float> progressCallback) throws IOException {
        try {
            Path updateArchive = downloader.downloadUpdate(version.downloadUrl(), downloadProgress -> progressCallback.accept(downloadProgress * 0.5f));
            // TODO: if checksum is given, verify it. If the checksum doesn't match, delete the downloaded file and throw an exception
            Path updateDir = unpacker.unpack(updateArchive, unpackProgress -> progressCallback.accept(unpackProgress * 0.5f + 0.5f));
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
