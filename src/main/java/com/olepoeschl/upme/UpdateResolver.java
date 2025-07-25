package com.olepoeschl.upme;

import java.io.IOException;

/**
 * Interface for resolving available updates for an application. That means it checks for downloadable versions and
 * gathers some information about them, in all cases including their version strings and download URLs.
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
