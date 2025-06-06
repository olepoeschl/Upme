package com.olepoeschl.upme.core;

import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

@NullMarked
public interface UpdateResolver {

    List<UpdateInfo> checkForUpdates();

    record UpdateInfo(String version, String downloadUrl, String description) {
        public UpdateInfo {
            Objects.requireNonNull(version, "version must not be null");
            Objects.requireNonNull(downloadUrl, "downloadUrl must not be null");
            Objects.requireNonNull(description, "description must not be null");
        }
    }
}
