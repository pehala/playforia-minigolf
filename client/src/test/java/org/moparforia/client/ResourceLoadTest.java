package org.moparforia.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


/**
 * Tests that resources can be loaded
 */
class ResourceLoadTest {

    /**
     * Tests that Launcher icon can be loaded
     */
    @Test
    void testLoadIcon() {
        Launcher launcher = new Launcher();
        assertDoesNotThrow(launcher::loadIcon);
    }

}