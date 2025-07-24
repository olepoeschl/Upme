package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a version of an application, including its version string, download URL, description, and an optional
 * SHA3-256 checksum for verification. Differentiates versions based only on the version string.
 * <p>
 * This class is used to encapsulate the details of a specific version of an application.
 * </p>
 *
 * @param versionString   required - the version string in semantic versioning format (e.g., "1.0.0")
 * @param downloadUrl     required - the URL from which the update can be downloaded
 * @param description     optional - a human-readable description of the version
 * @param checksumSha3256 optional - an optional SHA-256 checksum for verifying the integrity of the downloaded file
 * @version 1.0.0
 * @since 1.0.0
 */
@NullMarked
public record Version(String versionString, String downloadUrl, @Nullable String description,
                      @Nullable String checksumSha3256) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version other)) return false;
        return Objects.equals(versionString, other.versionString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionString);
    }
}
