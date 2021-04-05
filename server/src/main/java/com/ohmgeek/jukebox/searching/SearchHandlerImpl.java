package com.ohmgeek.jukebox.searching;

import com.ohmgeek.jukebox.domain.SearchSongRequest;
import com.ohmgeek.jukebox.domain.SearchSongResponse;
import com.ohmgeek.jukebox.domain.Song;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class SearchHandlerImpl implements Handler<Message<SearchSongRequest>> {
    private static final Logger logger = LoggerFactory.getLogger(SearchHandlerImpl.class);
    private final SearchBackend searchBackend;

    @Inject
    public SearchHandlerImpl(SearchBackend searchBackend) {
        this.searchBackend = searchBackend;
    }

    @Override
    public void handle(Message<SearchSongRequest> event) {
        SearchSongRequest request = event.body();

        // Run the search
        try {
            List<Song> search = searchBackend.search(request.getSearchTerm());

            SearchSongResponse.Builder builder = SearchSongResponse.newBuilder();
            // Now reply to sender.
            for (Song song : search) {
                builder.addSong(song);
            }

            // Build the response
            event.reply(builder.build());

        } catch (SearchException e) {
            event.fail(-1, e.getMessage());
        }

    }
}
