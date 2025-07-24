package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Objects;

// TODO: javadoc
@NullMarked
public record Version(String versionString, String downloadUrl, String description, @Nullable String checksumSha3256) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version other)) return false;
        return Objects.equals(versionString, other.versionString)
                && Objects.equals(checksumSha3256, other.checksumSha3256);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionString, checksumSha3256);
    }
}
