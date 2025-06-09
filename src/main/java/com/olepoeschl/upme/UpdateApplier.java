package com.olepoeschl.upme;

import java.nio.file.Path;

public interface UpdateApplier {

    void applyUpdate(Path updateFile);

    void cleanup();

}
