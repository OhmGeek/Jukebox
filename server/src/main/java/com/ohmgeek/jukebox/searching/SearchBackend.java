package com.ohmgeek.jukebox.searching;

import com.ohmgeek.jukebox.domain.Song;

import java.util.List;

public interface SearchBackend {
    List<Song> search(String searchTerm) throws SearchException;
}
