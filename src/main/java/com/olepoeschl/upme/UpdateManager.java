package com.olepoeschl.upme;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class UpdateManager {

    private final UpdateResolver updateResolver;
    private final UpdateProvider updateProvider;
    private final UpdateApplier updateApplier;

    public UpdateManager(UpdateResolver updateResolver, UpdateProvider updateProvider, UpdateApplier updateApplier) {
        this.updateResolver = Objects.requireNonNull(updateResolver, "updateResolver must not be null");
        this.updateProvider = Objects.requireNonNull(updateProvider, "updateProvider must not be null");
        this.updateApplier = Objects.requireNonNull(updateApplier, "updateApplier must not be null");
    }

    void initialize(String currentVersion, String updateServerUrl) {

    }

    boolean possible() {
        return false;
    }

    void now() {

    }

    public UpdateResolver getUpdateResolver() {
        return updateResolver;
    }

    public UpdateProvider getUpdateProvider() {
        return updateProvider;
    }

    public UpdateApplier getUpdateApplier() {
        return updateApplier;
    }
}
