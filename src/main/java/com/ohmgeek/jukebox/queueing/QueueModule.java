package com.ohmgeek.jukebox.queueing;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.ohmgeek.jukebox.queueing.handler.AddSongToQueueHandler;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;

public class QueueModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind Vertx services
        Multibinder<Verticle> verticleMultibinder = Multibinder.newSetBinder(binder(), Verticle.class);
        verticleMultibinder.addBinding().to(QueueService.class).asEagerSingleton();

        Multibinder<Handler> handlerMultibinder = Multibinder.newSetBinder(binder(), Handler.class, QueueHandler.class);
        handlerMultibinder.addBinding().to(AddSongToQueueHandler.class);
    }
}
