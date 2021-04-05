package com.ohmgeek.jukebox;

import com.google.inject.AbstractModule;
import com.ohmgeek.jukebox.common.CommonModule;
import com.ohmgeek.jukebox.queueing.QueueModule;
import com.ohmgeek.jukebox.searching.SearchModule;

public class JukeboxServerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new CommonModule());
        install(new SearchModule());
        install(new QueueModule());
    }
}
