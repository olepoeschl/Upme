package com.olepoeschl.upme;

import java.io.IOException;

/**
 * The UpdateResolver interface provides a method to check for available updates for an application.
 * It is used to determine which versions of the application are newer than the currently installed version.
 * Implementations of this interface should ideally consider the current version of the application to be able to
 * determine which available versions are updates (i.e., newer versions).
 *
 * @author Ole Pöschl
 * @version 1.0
 * @since 1.0
 */
public interface UpdateResolver {

    /**
     * Checks for updates, that means downloadable versions of the application that are newer than the current one.
     *
     * @param currentVersionString the current version of the application as a semantic versioning string
     * @return a list of available versions to update to, where each update is represented by a {@link Version} object
     * @since 1.0
     */
    Version[] checkAvailableUpdates(String currentVersionString) throws IOException;

}
