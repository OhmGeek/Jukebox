package com.ohmgeek.jukebox.common;

import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

import javax.inject.Inject;
import java.util.Set;

public class VerticleManager implements GuiceService{

    private final Vertx vertx;
    private final Set<Verticle> verticleSet;

    @Inject
    public VerticleManager(Vertx vertx,
                           Set<Verticle> verticleSet) {
        this.vertx = vertx;
        this.verticleSet = verticleSet;
    }

    @Override
    public void setUpService() {
        verticleSet.forEach((vertx::deployVerticle));
    }

    @Override
    public void tearDownService() {
        vertx.close();
    }
}
