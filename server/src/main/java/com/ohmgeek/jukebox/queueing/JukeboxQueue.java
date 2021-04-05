package com.ohmgeek.jukebox.queueing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ForwardingBlockingQueue;
import com.ohmgeek.jukebox.domain.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * I represent a jukebox queue.
 */
public class JukeboxQueue {
    private static final Logger logger = LoggerFactory.getLogger(JukeboxQueue.class);
    private final Queue<Song> queue;

    public JukeboxQueue() {
        queue = Queues.newConcurrentLinkedQueue();
    }

    public void addSong(Song song) throws QueueManagementException {
        queue.add(song);
        logger.info("Song added to queue");
    }

    public int size() {
        return queue.size();
    }

    public ImmutableList<Object> getQueue() {
        return ImmutableList.copyOf(queue.toArray());
    }

    public Song fetchNextSongToPlay() throws QueueManagementException {
        return queue.remove();
    }
}
