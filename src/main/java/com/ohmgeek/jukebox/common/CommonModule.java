package com.ohmgeek.jukebox.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;

public class CommonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(VerticleManager.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public Vertx providesVertxInstance() {
        return Vertx.vertx();
    }
}
