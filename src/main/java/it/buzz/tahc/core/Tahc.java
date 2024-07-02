package it.buzz.tahc.core;

import it.buzz.tahc.bootstraps.TahcBootstrap;
import it.buzz.tahc.core.commands.TahcCommands;
import it.buzz.tahc.core.service.PluginService;
import it.buzz.tahc.core.service.impl.CheckService;
import it.buzz.tahc.core.service.impl.ConfigurationService;
import it.buzz.tahc.core.service.impl.SlowdownService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Tahc {

    private final Map<Class<? extends PluginService<?>>, PluginService<?>> services = new LinkedHashMap<>();
    @Getter private final TahcBootstrap bootstrap;
    @Getter private TahcCommands commands;

    public boolean enable(){
        for (Class<?> service : getServices()) {
            try {
                if (service.isAssignableFrom(PluginService.class))
                    throw new RuntimeException();

                PluginService<?> object = (PluginService<?>) service.getConstructor(Tahc.class).newInstance(this);
                object.start();

                services.put((Class<? extends PluginService<?>>) service, object);
                bootstrap.log("Loaded a new service: " + service.getSimpleName());
            } catch (Exception e) {
                e.printStackTrace();
                bootstrap.log("Cannot load a service: " + service.getSimpleName());
                bootstrap.log("Instance shutdown...");
                return false;
            }
        }

        commands = new TahcCommands(this);

        return true;
    }
    public void disable(){
        services.values().forEach(PluginService::stop);
        services.clear();
    }

    private Class<?>[] getServices() {
        return new Class<?>[]{
                ConfigurationService.class,
                SlowdownService.class,
                CheckService.class
        };
    }
    private <T> T getService(Class<T> clazz) {
        return (T) services.get(clazz);
    }

    public CheckService getCheckService() {
        return getService(CheckService.class);
    }
    public ConfigurationService getConfigurationService() {
        return getService(ConfigurationService.class);
    }
    public SlowdownService getSlowdownService() {
        return getService(SlowdownService.class);
    }

}
