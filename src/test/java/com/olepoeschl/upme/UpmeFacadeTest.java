package com.olepoeschl.upme;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class UpmeFacadeTest {

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SetAndGetUpdateManager {
        @Test
        void givenValidUpdateManager_thenUpdateManagerIsSet() {
            var mockUpdateManager = new UpdateManager() {
                @Override
                public void initialize(String currentVersion, String updateServerUrl) {}
                @Override
                public boolean possible() {
                    return false;
                }
                @Override
                public void now() {}
            };
            Upme.setUpdateManager(mockUpdateManager);
            assertSame(mockUpdateManager, Upme.getUpdateManager());
        }

        @Test
        void givenNull_thenThrowNullPointerException() {
            assertThrows(NullPointerException.class, () -> Upme.setUpdateManager(null));
        }

        // this test should execute first because it tests the initial state
        @Test
        @Order(1)
        void givenNoUpdateManagerSet_thenUpdateManagerIsNotNull() {
            assertNotNull(Upme.getUpdateManager());
        }
    }

    @Nested
    class ForwardMethodCallsToUpdateManager {
        @Test
        void forwardInitialize() {
            var mockUpdateManager = mock(UpdateManager.class);
            Upme.setUpdateManager(mockUpdateManager);
            Upme.initialize("current_version", "update.server.url");
            verify(mockUpdateManager).initialize("current_version", "update.server.url");
        }

        @Test
        void forwardPossible() {
            var mockUpdateManager = mock(UpdateManager.class);
            when(mockUpdateManager.possible()).thenReturn(true);
            Upme.setUpdateManager(mockUpdateManager);
            assertTrue(Upme.possible());
            verify(mockUpdateManager).possible();
        }

        @Test
        void forwardNow() {
            var mockUpdateManager = mock(UpdateManager.class);
            Upme.setUpdateManager(mockUpdateManager);
            Upme.now();
            verify(mockUpdateManager).now();
        }
    }

}
