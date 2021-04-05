package com.ohmgeek.jukebox.searching;

import com.ohmgeek.jukebox.domain.SearchSongRequest;
import com.ohmgeek.jukebox.queueing.QueueHandler;
import com.ohmgeek.jukebox.queueing.QueueService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

import static com.ohmgeek.jukebox.common.Namespaces.SERVER_TO_CLIENT;

public class SearchService extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
    private final Set<Handler> handlers;

    @Inject
    public SearchService(@SearchHandler Set<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void start() {
        // start up exposed handlers
        handlers.forEach(handler -> getVertx().eventBus().localConsumer(SearchSongRequest.class.getSimpleName(), handler));
    }


    @Override
    public void stop() {
        // stop dependencies as required
    }

}