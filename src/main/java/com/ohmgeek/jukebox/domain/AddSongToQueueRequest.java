package com.ohmgeek.jukebox.domain;

public final class AddSongToQueueRequest extends DomainInstance {
    public Song getSong() {
        return new Song();
    }
}
