package com.olepoeschl.upme;

import java.util.List;

public interface UpdateResolver {
    List<UpdateInfo> getAvailableUpdates(String currentVersion);
}
