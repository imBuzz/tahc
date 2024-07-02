package it.buzz.tahc.bootstraps.bungee.scheduler;

import it.buzz.tahc.bootstraps.TahcScheduler;
import it.buzz.tahc.bootstraps.bungee.TahcBungeeBootstrap;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BungeeTahcScheduler implements TahcScheduler {

    private final TahcBungeeBootstrap bootstrap;

    @Override
    public void later(Runnable runnable, long delayInMillis) {
        bootstrap.getProxy().getScheduler().schedule(bootstrap, runnable, delayInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void laterTimer(Runnable runnable, long delayInMillis, long repeatInMillis) {
        bootstrap.getProxy().getScheduler().schedule(bootstrap, runnable, delayInMillis, repeatInMillis, TimeUnit.MILLISECONDS);
    }

}
