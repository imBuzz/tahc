package it.buzz.tahc.core.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.service.PluginService;
import net.kyori.adventure.audience.Audience;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SlowdownService extends PluginService<Tahc> {

    private Cache<Audience, String> slowdownPlayers;

    public SlowdownService(Tahc plugin) {
        super(plugin);
    }

    @Override
    public void start() {
        int timing = plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.SLOWDOWN_TIMING);
        if (timing != -1)
            slowdownPlayers = Caffeine.newBuilder()
                .expireAfterWrite(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.SLOWDOWN_TIMING), TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public void stop() {

    }

    public boolean checkPlayer(Audience audience, boolean bypass){
        if (plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.BYPASS_SLOWDOWN_PERMISSION) && bypass)
            return false;

        if (slowdownPlayers == null)
            return false;

        boolean result = slowdownPlayers.asMap().containsKey(audience);
        if (!result)
            slowdownPlayers.put(audience, "");

        return result;
    }


}
