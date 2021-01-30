package com.ohmgeek.jukebox.common;

public enum Namespaces {
    CLIENT_TO_SERVER("clientToServer"), SERVER_TO_CLIENT("serverToClient");

    private final String response;
    Namespaces(String response) {
        this.response = response;
    }
}
