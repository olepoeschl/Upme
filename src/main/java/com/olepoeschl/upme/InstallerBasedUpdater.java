package com.olepoeschl.upme;

import java.io.IOException;
import java.util.function.Consumer;

public class InstallerBasedUpdater implements Updater {
    @Override
    public Version[] checkAvailableUpdates() throws IOException {
        return new Version[0]; // TODO
    }

    @Override
    public void prepareUpdate(Version version, Consumer<Float> progressCallback) throws IOException {
        // TODO
    }

    @Override
    public Process applyUpdateAsync(Version version) {
        return null; // TODO
    }
}
