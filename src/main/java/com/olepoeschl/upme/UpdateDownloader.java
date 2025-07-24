package com.olepoeschl.upme;

import java.nio.file.Path;
import java.util.function.Consumer;

public interface UpdateDownloader {

    /**
     * Downloads the update from the specified URL and returns the path to the downloaded file.
     *
     * @param downloadUrl the URL from which to download the update
     * @param progressCallback a callback function that receives progress updates as a float value, should be between
     *                         0.0 and 1.0
     * @return the path to the downloaded update file
     *
     * @since 1.0
     */
    Path downloadUpdate(String downloadUrl, Consumer<Float> progressCallback);

}
