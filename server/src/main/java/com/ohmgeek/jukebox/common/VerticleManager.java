package com.ohmgeek.jukebox.common;

import com.ohmgeek.jukebox.common.codec.GenericCodec;
import com.ohmgeek.jukebox.domain.AddSongToQueueRequest;
import com.ohmgeek.jukebox.domain.AddSongToQueueResponse;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

public class VerticleManager implements GuiceService {

    private final Vertx vertx;
    private final Set<Verticle> verticleSet;
    private final Map<Class, GenericCodec> objectGenericCodecMap;

    @Inject
    public VerticleManager(Vertx vertx,
                           Set<Verticle> verticleSet,
                           Map<Class, GenericCodec> objectGenericCodecMap) {
        this.vertx = vertx;
        this.verticleSet = verticleSet;
        this.objectGenericCodecMap = objectGenericCodecMap;
    }

    @Override
    public void setUpService() {
        // First, we register codecs.
        objectGenericCodecMap.entrySet().forEach((entry) -> {
            vertx.eventBus().registerDefaultCodec(entry.getKey(), entry.getValue());
        });

        verticleSet.forEach((vertx::deployVerticle));

    }

    @Override
    public void tearDownService() {
        vertx.close();
    }
}
