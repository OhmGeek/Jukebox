package com.ohmgeek.jukebox.queueing;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ForwardingBlockingQueue;
import com.google.inject.Singleton;
import com.ohmgeek.jukebox.domain.Song;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Singleton
public class JukeboxQueue {
    private final ForwardingBlockingQueue<Song> queue;

    public JukeboxQueue() {
        queue = new ForwardingBlockingQueue<Song>() {
            @Override
            protected BlockingQueue<Song> delegate() {
                return new ArrayBlockingQueue<>(100);
            }
        };
    }

    public void addSong(Song song) throws QueueManagementException {
        try {
            queue.put(song);
        } catch (InterruptedException e) {
            throw new QueueManagementException();
        }
    }

    public ImmutableList<Object> getQueue() {
        return ImmutableList.copyOf(queue.toArray());
    }

    public Song fetchNextSongToPlay() throws QueueManagementException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new QueueManagementException();
        }
    }
}
