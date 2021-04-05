package com.ohmgeek.jukebox;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.ohmgeek.jukebox.common.CommonModule;
import com.ohmgeek.jukebox.common.VerticleManager;
import com.ohmgeek.jukebox.domain.MusicBackend;
import com.ohmgeek.jukebox.domain.SearchSongRequest;
import com.ohmgeek.jukebox.domain.SearchSongResponse;
import com.ohmgeek.jukebox.domain.Song;
import com.ohmgeek.jukebox.queueing.QueueModule;
import com.ohmgeek.jukebox.searching.SearchException;
import com.ohmgeek.jukebox.searching.SearchModule;
import com.ohmgeek.jukebox.searching.YouTubeSearchBackend;
import io.vertx.core.Vertx;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class YouTubeSearchTest {
    private static Injector injector;

    @Inject
    private Vertx vertx;
    @Inject
    private YouTubeSearchBackend youTubeSearchBackend;

    @BeforeAll
    public static void beforeAll() {
        injector = Guice.createInjector(new CommonModule(), new QueueModule(), new SearchModule());
        injector.getInstance(VerticleManager.class).setUpService();
    }

    @BeforeEach
    public void before() {
        injector.injectMembers(this);
    }

    @Test
    public void canSearchViaBackend() throws SearchException {
        // First, we run the search.
        List<Song> songs = youTubeSearchBackend.search("test");

        // We expect 10 results here.
        assertThat(songs.size(), is(10));

        Song exampleSong = songs.get(0);

        // Verify a song contains roughly the right information (e.g. name, URL and the backend sent correctly).
        assertThat(exampleSong.getName(), not(isEmptyOrNullString()));
        assertThat(exampleSong.getUrl(), not(isEmptyOrNullString()));
        assertThat(exampleSong.getBackend(), is(MusicBackend.YOUTUBE));
    }

    @Test
    public void canSearchViaRequest() throws InterruptedException {
        SearchSongRequest request = SearchSongRequest.newBuilder()
                .setSearchTerm("test")
                .build();

        CountDownLatch latch = new CountDownLatch(1);

        vertx.eventBus().request(SearchSongRequest.class.getSimpleName(), request, (response) -> {
            assertFalse(response.failed());
            Object resultBody = response.result().body();
            assertThat(resultBody, instanceOf(SearchSongResponse.class));

            SearchSongResponse realResponse = (SearchSongResponse) resultBody;

            assertThat(realResponse.getSongCount(), is(10));
            assertThat(realResponse.getSongList().get(0).getBackend(), is(MusicBackend.YOUTUBE));
            assertThat(realResponse.getSongList().get(0).getName(), not(isEmptyOrNullString()));
            assertThat(realResponse.getSongList().get(0).getUrl(), not(isEmptyOrNullString()));

            latch.countDown();
        });

        // Now we wait. If we timeout, then we fail the test.
        latch.await(5, TimeUnit.SECONDS);
    }

}
