package com.ohmgeek.jukebox.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.ohmgeek.jukebox.common.codec.GenericCodec;
import com.ohmgeek.jukebox.domain.AddSongToQueueRequest;
import com.ohmgeek.jukebox.domain.AddSongToQueueResponse;
import com.ohmgeek.jukebox.domain.SearchSongRequest;
import com.ohmgeek.jukebox.domain.SearchSongResponse;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CommonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(VerticleManager.class).asEagerSingleton();

        TypeLiteral<Class> objectType = TypeLiteral.get(Class.class);
        TypeLiteral<GenericCodec> codecType = TypeLiteral.get(GenericCodec.class);

        MapBinder<Class, GenericCodec> codecMultibinder = MapBinder.newMapBinder(binder(),
                objectType,
                codecType);

        // We map domain objects (requests/responses) to VertX Codecs (to define how to send/receive over the local EventBus)
        codecMultibinder.addBinding(AddSongToQueueRequest.class)
                .toInstance(new GenericCodec<>(AddSongToQueueRequest.class));
        codecMultibinder.addBinding(AddSongToQueueResponse.class)
                .toInstance(new GenericCodec<>(AddSongToQueueResponse.class));

        codecMultibinder.addBinding(SearchSongRequest.class)
                .toInstance(new GenericCodec<>(SearchSongRequest.class));
        codecMultibinder.addBinding(SearchSongResponse.class)
                .toInstance(new GenericCodec<>(SearchSongResponse.class));
    }

    @Provides
    @Singleton
    public Vertx providesVertxInstance() {
        return Vertx.vertx();
    }

    @Provides
    @Singleton
    @ServerConfig
    public JsonObject providesConfig(Vertx vertx) {
        ConfigRetriever configRetriever = ConfigRetriever.create(vertx);
        return configRetriever.getConfig().result();
    }
}
