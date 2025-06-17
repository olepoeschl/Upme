package com.olepoeschl.upme;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateManagerTest {

    private final UpdateResolver nullResolver = currentVersion -> null;
    private final UpdateProvider nullProvider = updateInfo -> null;
    private final UpdateApplier nullApplier = updatePath -> null;

    @Nested
    class Constructor {
        @Test
        void givenValidDependencies_thenInjectThem() {
            var updateManager = new UpdateManager(nullResolver, nullProvider, nullApplier);
            assertSame(nullResolver, updateManager.getUpdateResolver());
            assertSame(nullProvider, updateManager.getUpdateProvider());
            assertSame(nullApplier, updateManager.getUpdateApplier());
        }
        @Test
        void givenNullDependencies_thenThrowException() {
            assertThrows(NullPointerException.class, () -> new UpdateManager(null, nullProvider, nullApplier));
            assertThrows(NullPointerException.class, () -> new UpdateManager(nullResolver, null, nullApplier));
            assertThrows(NullPointerException.class, () -> new UpdateManager(nullResolver, nullProvider, null));
        }
    }

}
