package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record Version(String version, String downloadUrl, String description, @Nullable String checksumSha3256) {
}
