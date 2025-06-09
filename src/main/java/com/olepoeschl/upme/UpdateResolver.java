package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

@NullMarked
public interface UpdateResolver {

    /**
     * Gathers a list of available updates from the update server.
     *
     * @return A list of {@link UpdateInfo} objects containing details about available updates.
     */
    List<UpdateInfo> checkForUpdates();

    record UpdateInfo(String version, String downloadUrl, String description) {
        public UpdateInfo {
            Objects.requireNonNull(version, "version must not be null");
            Objects.requireNonNull(downloadUrl, "downloadUrl must not be null");
            Objects.requireNonNull(description, "description must not be null");
        }
    }
}
