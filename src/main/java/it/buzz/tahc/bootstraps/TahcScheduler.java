package it.buzz.tahc.bootstraps;

public interface TahcScheduler {

    void later(Runnable runnable, long delayInMillis);
    void laterTimer(Runnable runnable, long delayInMillis, long repeatInMillis);

}
