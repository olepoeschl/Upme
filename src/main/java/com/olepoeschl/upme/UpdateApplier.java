package com.olepoeschl.upme;

import java.nio.file.Path;

public interface UpdateApplier {
    Process applyUpdate(Path updatePath);
}
