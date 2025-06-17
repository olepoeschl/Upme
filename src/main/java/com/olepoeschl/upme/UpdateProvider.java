package com.olepoeschl.upme;

import java.nio.file.Path;

public interface UpdateProvider {
    Path provideUpdate(UpdateInfo updateInfo);
}
