package com.ohmgeek.jukebox.common;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ohmgeek.jukebox.JukeboxServerModule;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractGuiceServiceDaemon extends AbstractIdleService {
    private final Injector injector;
    private List<Class<? extends GuiceService>> guiceServices;

    public AbstractGuiceServiceDaemon(Module... modules) {
        // Create the injector for the provided modules
        injector = Guice.createInjector(modules);
        guiceServices = Lists.newArrayList();
    }

    public void registerInOrder(Class<? extends GuiceService>... services) {
        guiceServices.addAll(Arrays.asList(services));
    }

    @Override
    protected void startUp() throws Exception {
        guiceServices.forEach((ref) -> injector.getInstance(ref).setUpService());
    }

    @Override
    protected void shutDown() throws Exception {
        if (injector != null) {
            guiceServices.forEach((ref) -> injector.getInstance(ref).tearDownService());
        }
    }

    public void start() {
        this.startAsync();
        this.awaitTerminated();
    }

    @Override
    protected String serviceName() {
        return getClass().getSimpleName();
    }
}
