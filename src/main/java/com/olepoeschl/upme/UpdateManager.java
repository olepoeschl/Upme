package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface UpdateManager {

    void initialize(@Nullable String currentVersion, String updateServerUrl);

    boolean possible();

    void now();

}
