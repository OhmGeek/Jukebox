package com.ohmgeek.jukebox.searching;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.ohmgeek.jukebox.domain.MusicBackend;
import com.ohmgeek.jukebox.domain.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class YouTubeSearchBackend implements SearchBackend {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeSearchBackend.class);

    public static final String YOUTUBE_API_KEY = "youtube-api-key";

    // We should cache search terms here, so that we don't hit the API too often
    private final Cache<String, List<Song>> searchSongCache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .maximumSize(5000)
            .build();

    private final YouTube youTube;
    private final String youTubeApiKey;

    @Inject
    public YouTubeSearchBackend(YouTube youTube,
                                @Named(YOUTUBE_API_KEY) String youTubeApiKey) {
        this.youTube = youTube;
        this.youTubeApiKey = youTubeApiKey;
    }

    @Override
    public List<Song> search(String searchTerm) throws SearchException {
        try {
            // We fetch from the cache, falling back to the YT call.
            return searchSongCache.get(searchTerm.toLowerCase(Locale.ROOT),
                    () -> handleApiSearch(searchTerm));
        } catch (ExecutionException e) {
            throw new SearchException();
        }
    }

    /**
     * Handle a search term
     *
     * @param term the search term
     * @return the list of songs for a specific search term
     */
    private List<Song> handleApiSearch(String term) {
        logger.info("Searching {} via the YT API.", term);
        try {
            SearchListResponse searchResults = youTube.search()
                    .list(Lists.newArrayList("snippet", "id"))
                    .setQ(term)
                    .setKey(youTubeApiKey)
                    .setMaxResults(10L)
                    .setType(Lists.newArrayList("video"))
                    .setVideoDuration("short")
                    .execute();

            return searchResults
                    .getItems()
                    .stream()
                    // We map these to our domain object (stripping out the metadata)
                    .map((result) -> Song.newBuilder()
                            .setBackend(MusicBackend.YOUTUBE)
                            .setName(result.getSnippet().getTitle())
                            .setUrl(result.getId().getVideoId())
                            .build()).collect(Collectors.toList());

        } catch (IOException e) {
            logger.error("Error searching {} via the YT API", term, e);
            throw new RuntimeException(e);
        }
    }
}
