package it.buzz.tahc.core.service;

import it.buzz.tahc.core.Tahc;

public abstract class PluginService<T extends Tahc> {

    protected final T plugin;

    protected PluginService(T plugin) {
        this.plugin = plugin;
    }

    public abstract void start();
    public abstract void stop();

}
