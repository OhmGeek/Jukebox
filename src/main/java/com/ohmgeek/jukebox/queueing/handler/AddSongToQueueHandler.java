package com.ohmgeek.jukebox.queueing.handler;

import com.ohmgeek.jukebox.domain.AddSongToQueueRequest;
import com.ohmgeek.jukebox.queueing.JukeboxQueue;
import com.ohmgeek.jukebox.queueing.QueueHandler;
import com.ohmgeek.jukebox.queueing.QueueManagementException;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

import javax.inject.Inject;

@QueueHandler
public class AddSongToQueueHandler implements Handler<Message<AddSongToQueueRequest>> {
    private final JukeboxQueue queue;

    @Inject
    public AddSongToQueueHandler(JukeboxQueue queue) {
        this.queue = queue;
    }

    @Override
    public void handle(Message<AddSongToQueueRequest> addSongToQueueHandlerMessage) {
        AddSongToQueueRequest request = addSongToQueueHandlerMessage.body();

        // Try adding the song, returning any error to the user via the bridge.
        try {
            queue.addSong(request.getSong());
        } catch (QueueManagementException e) {
            addSongToQueueHandlerMessage.fail(0, e.getMessage());
        }
    }
}
