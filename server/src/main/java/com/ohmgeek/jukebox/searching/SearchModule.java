package com.ohmgeek.jukebox.searching;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;

import javax.inject.Named;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.ohmgeek.jukebox.searching.YouTubeSearchBackend.YOUTUBE_API_KEY;

public class SearchModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind Vertx services
        Multibinder<Verticle> verticleMultibinder = Multibinder.newSetBinder(binder(), Verticle.class);
        verticleMultibinder.addBinding().to(SearchService.class).asEagerSingleton();

        Multibinder<Handler> handlerMultibinder = Multibinder.newSetBinder(binder(), Handler.class, SearchHandler.class);
        handlerMultibinder.addBinding().to(SearchHandlerImpl.class);

        // For now, we only support YouTube. We should eventually be able to delegate searching to multiple backends as required.
        bind(SearchBackend.class).to(YouTubeSearchBackend.class).asEagerSingleton();
    }

    @Provides
    @Named(YOUTUBE_API_KEY)
    public String provideYouTubeAccessToken() {
        return System.getProperty("backend.youtube.key", "");
    }

    @Provides
    @Singleton
    public ApacheHttpTransport provideTransport() throws GeneralSecurityException, IOException {
        return new ApacheHttpTransport();
    }

    @Provides
    @Singleton
    public GsonFactory provideGsonFactory() {
        return new GsonFactory();
    }

    @Provides
    public YouTube provideYouTube(ApacheHttpTransport httpTransport,
                                  GsonFactory gsonFactory) {

        YouTube.Builder builder = new YouTube.Builder(httpTransport, gsonFactory, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        });

        return builder.setApplicationName("Jukebox Server")
                .build();
    }
}
