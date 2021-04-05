package com.ohmgeek.jukebox.websocket;

import io.socket.socketio.server.SocketIoNamespace;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.util.Arrays;

import static com.ohmgeek.jukebox.common.Namespaces.CLIENT_TO_SERVER;
import static com.ohmgeek.jukebox.common.Namespaces.SERVER_TO_CLIENT;

/**
 * This is a proxy between the Vertx internal EventBus, and the Web Socket protocol we have defined in our API.
 *
 * TODO: this needs to be reengineered to convert JSON to the domain objects we are using.
 */
public class VertxWebsocketProxy extends AbstractVerticle {

    private final SocketIoNamespace socketIoNamespace;

    public VertxWebsocketProxy(SocketIoNamespace socketIoNamespace) {
        this.socketIoNamespace = socketIoNamespace;
    }

    @Override
    public void start() {
        // First, handle server -> client
        getVertx().eventBus().localConsumer(SERVER_TO_CLIENT.name(), (obj) -> {
            // First, take the object
            Object body = obj.body();
            socketIoNamespace.emit(body.getClass().getSimpleName(), body);
        });

        // Now we handle the opposite (client -> server)
        // Map all events and write them into Vertx.
        EventBus eventBus = getVertx().eventBus();
        socketIoNamespace.on("*", (objects) -> {
            Arrays.stream(objects)
                    .forEach((obj) -> eventBus.publish(CLIENT_TO_SERVER.name(), obj));
        });


    }

    @Override
    public void stop() {

    }
}
