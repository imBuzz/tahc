package it.buzz.tahc.bootstraps.spigot.scheduler;

import it.buzz.tahc.bootstraps.TahcScheduler;
import it.buzz.tahc.bootstraps.spigot.TahcSpigotBootstrap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpigotTahcScheduler implements TahcScheduler {

    private final TahcSpigotBootstrap bootstrap;

    @Override
    public void later(Runnable runnable, long delayInMillis) {
        bootstrap.getServer().getScheduler().runTaskLater(bootstrap, runnable, delayInMillis / 50);
    }

    @Override
    public void laterTimer(Runnable runnable, long delayInMillis, long repeatInMillis) {
        bootstrap.getServer().getScheduler().runTaskTimer(bootstrap, runnable, delayInMillis / 50, repeatInMillis / 50);
    }

}
