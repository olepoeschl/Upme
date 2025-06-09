package com.olepoeschl.upme;

import java.nio.file.Path;

public interface UpdateProvider {

    /**
     * Downloads the update file from the specified URL.
     *
     * @param updateUrl The URL of the update file to be downloaded.
     * @return The path to the update file.
     */
    Path downloadUpdate(String updateUrl);
}
