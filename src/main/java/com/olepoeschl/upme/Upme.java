package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class Upme {

    private static UpdateManager updateManager = new UpdateManager() {
        @Override
        public void initialize(String currentVersion, String updateServerUrl) {}
        @Override
        public boolean possible() {
            return false;
        }
        @Override
        public void now() {}
    };

    public static void setUpdateManager(UpdateManager updateManager) {
        Upme.updateManager = Objects.requireNonNull(updateManager);
    }

    public static UpdateManager getUpdateManager() {
        return updateManager;
    }

    public static void initialize(@Nullable String currentVersion, String updateServerUrl) {
        updateManager.initialize(currentVersion, updateServerUrl);
    }

    public static boolean possible() {
        return updateManager.possible();
    }

    public static void now() {
        updateManager.now();
    }
}
