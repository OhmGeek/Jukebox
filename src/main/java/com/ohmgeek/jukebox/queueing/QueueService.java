package com.ohmgeek.jukebox.queueing;

import com.ohmgeek.jukebox.domain.AddSongToQueueRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.rxjava.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

/**
 * This service manages the local queue of songs to be played.
 */
public class QueueService extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
    private final Set<Handler> handlers;

    @Inject
    public QueueService(@QueueHandler Set<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void start() {
        // start up dependencies as required.
        handlers.forEach(handler -> {
            getVertx().eventBus().localConsumer(handler.getClass().getSimpleName(), handler);
        });
    }


    @Override
    public void stop() {
        // stop dependencies as required
    }

}
