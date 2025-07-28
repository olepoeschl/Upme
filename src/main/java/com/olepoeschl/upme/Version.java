package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a version of an application, including its version string, download URL, an optional description, and an
 * optional checksum, e.g. SHA256, for verification. Differentiates versions based only on the version string.
 * <p>
 * This class is used to encapsulate the details of a specific version of an application.
 * </p>
 *
 * @param versionString   required - the version string in semantic versioning format (e.g., "1.0.0")
 * @param downloadUrl     required - the URL from which the update can be downloaded
 * @param description     optional - a human-readable description of the version
 * @param checksum optional - an optional SHA-256 checksum for verifying the integrity of the downloaded file
 * @version 1.0.0
 * @since 1.0.0
 */
@NullMarked
public record Version(String versionString, String downloadUrl, @Nullable String description,
                      @Nullable String checksum) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version(String string, String url, String description1, String checksum1))) return false;
        return Objects.equals(versionString, string)
            && Objects.equals(downloadUrl, url)
            && Objects.equals(description, description1)
            && Objects.equals(checksum, checksum1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionString, downloadUrl, description, checksum);
    }
}
