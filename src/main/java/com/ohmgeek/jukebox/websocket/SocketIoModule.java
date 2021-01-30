package com.ohmgeek.jukebox.websocket;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.xml.internal.ws.api.pipe.Engine;
import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;

public class SocketIoModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(VertxWebsocketProxy.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public SocketIoNamespace provideSocketIoNamespace() {
        SocketIoServer socketIoServer = new SocketIoServer(new EngineIoServer());
        return socketIoServer.namespace("*");
    }
}
