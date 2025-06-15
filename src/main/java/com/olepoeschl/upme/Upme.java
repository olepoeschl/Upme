package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public final class Upme {

    private static UpdateManager updateManager = new UpdateManager();

    public static void setUpdateManager(UpdateManager updateManager) {
        Upme.updateManager = Objects.requireNonNull(updateManager);
    }

    public static UpdateManager getUpdateManager() {
        return updateManager;
    }

    public static void initialize(String currentVersion, String updateServerUrl) {
        updateManager.initialize(currentVersion, updateServerUrl);
    }

    public static boolean possible() {
        return updateManager.possible();
    }

    public static void now() {
        updateManager.now();
    }
}
