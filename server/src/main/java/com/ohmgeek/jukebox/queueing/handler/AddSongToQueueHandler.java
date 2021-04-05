package com.ohmgeek.jukebox.queueing.handler;

import com.ohmgeek.jukebox.domain.AddSongToQueueRequest;
import com.ohmgeek.jukebox.queueing.JukeboxQueue;
import com.ohmgeek.jukebox.queueing.QueueHandler;
import com.ohmgeek.jukebox.queueing.QueueManagementException;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@QueueHandler
public class AddSongToQueueHandler implements Handler<Message<AddSongToQueueRequest>> {
    private static final Logger logger = LoggerFactory.getLogger(AddSongToQueueHandler.class);
    private final JukeboxQueue queue;

    @Inject
    public AddSongToQueueHandler(JukeboxQueue queue) {
        this.queue = queue;
    }

    @Override
    public void handle(Message<AddSongToQueueRequest> addSongToQueueHandlerMessage) {
        AddSongToQueueRequest request = addSongToQueueHandlerMessage.body();
        logger.info("Starting request, requestDetails={}", request);
        // Try adding the song, returning any error to the user via the bridge.
        try {
            queue.addSong(request.getSong());
            logger.info("Song added");
        } catch (QueueManagementException e) {
            logger.error("Failed to add song to queue", e);
            addSongToQueueHandlerMessage.fail(0, e.getMessage());
        }
    }
}
