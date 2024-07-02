package it.buzz.tahc.bootstraps;

import net.kyori.adventure.audience.Audience;

import java.io.File;
import java.util.Collection;

public interface TahcBootstrap {

    TahcScheduler getScheduler();

    File getBootstrapDataFolder();
    Collection<Audience> getAudibleAudience();

    void log(String log);
    void executeCommand(String s);

}
