package com.ohmgeek.jukebox.queueing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

import static com.ohmgeek.jukebox.common.Namespaces.SERVER_TO_CLIENT;

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
        // start up exposed handlers
        handlers.forEach(handler -> getVertx().eventBus().localConsumer(SERVER_TO_CLIENT.name(), handler));
    }


    @Override
    public void stop() {
        // stop dependencies as required
    }

}
