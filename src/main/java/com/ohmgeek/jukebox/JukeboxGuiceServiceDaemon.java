package com.ohmgeek.jukebox;

import com.ohmgeek.jukebox.common.AbstractGuiceServiceDaemon;
import com.ohmgeek.jukebox.common.VerticleManager;

public class JukeboxGuiceServiceDaemon extends AbstractGuiceServiceDaemon {
    public static void main(String... args) {
        JukeboxGuiceServiceDaemon jukeboxGuiceServiceDaemon = new JukeboxGuiceServiceDaemon();

        jukeboxGuiceServiceDaemon.registerInOrder(
                VerticleManager.class
        );
        jukeboxGuiceServiceDaemon.start();
    }
}
