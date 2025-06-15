package com.olepoeschl.upme;

public interface UpdateManager {

    void initialize(String currentVersion, String updateServerUrl);

    boolean possible();

    void now();

}
