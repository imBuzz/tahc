package it.buzz.tahc.bootstraps.velocity.scheduler;

import it.buzz.tahc.bootstraps.TahcScheduler;
import it.buzz.tahc.bootstraps.velocity.TahcVelocityBootstrap;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class VelocityTahcScheduler implements TahcScheduler {

    private final TahcVelocityBootstrap bootstrap;

    @Override
    public void later(Runnable runnable, long delayInMillis) {
        bootstrap.getServer().getScheduler()
                .buildTask(bootstrap, runnable)
                .delay(delayInMillis, TimeUnit.MILLISECONDS)
                .schedule();
    }

    @Override
    public void laterTimer(Runnable runnable, long delayInMillis, long repeatInMillis) {
        bootstrap.getServer().getScheduler()
                .buildTask(bootstrap, runnable)
                .delay(delayInMillis, TimeUnit.MILLISECONDS)
                .repeat(repeatInMillis, TimeUnit.MILLISECONDS)
                .schedule();
    }
}
