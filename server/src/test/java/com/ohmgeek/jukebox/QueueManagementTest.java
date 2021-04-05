package com.ohmgeek.jukebox;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.ohmgeek.jukebox.common.CommonModule;
import com.ohmgeek.jukebox.common.VerticleManager;
import com.ohmgeek.jukebox.domain.AddSongToQueueRequest;
import com.ohmgeek.jukebox.domain.MusicBackend;
import com.ohmgeek.jukebox.domain.Song;
import com.ohmgeek.jukebox.queueing.JukeboxQueue;
import com.ohmgeek.jukebox.queueing.QueueManagementException;
import com.ohmgeek.jukebox.queueing.QueueModule;
import io.vertx.core.Vertx;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueueManagementTest {

    private static Injector injector;

    @Inject
    private Vertx vertx;
    @Inject
    private JukeboxQueue jukeboxQueue;

    @BeforeAll
    public static void beforeAll() {
        injector = Guice.createInjector(new CommonModule(), new QueueModule());
        injector.getInstance(VerticleManager.class).setUpService();
    }

    @BeforeEach
    public void before() {
        injector.injectMembers(this);
    }

    @Test
    public void shouldAddSongToQueueViaHandler() throws InterruptedException, QueueManagementException {
        // Given a request
        AddSongToQueueRequest request = AddSongToQueueRequest.newBuilder()
                .setSong(Song.newBuilder()
                        .setBackend(MusicBackend.YOUTUBE)
                        .setName("MySong")
                        .setUrl("site/askdfhkjadf"))
                .build();

        assertEquals(0, jukeboxQueue.getQueue().size());

        // When we publish a message to the EventBus.
        vertx.eventBus().publish(AddSongToQueueRequest.class.getSimpleName(), request);

        await().atMost(5, TimeUnit.SECONDS).until(() -> jukeboxQueue.size() == 1);

        // Then we should have more than one song.
        assertEquals(1, jukeboxQueue.size());

        // Now take this from the queue. We should find the song we added
        assertThat(jukeboxQueue.fetchNextSongToPlay(), CoreMatchers.is(request.getSong()));

        // We should have no more elements.
        assertEquals(0, jukeboxQueue.size());
    }

}
