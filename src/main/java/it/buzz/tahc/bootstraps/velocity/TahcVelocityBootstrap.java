package it.buzz.tahc.bootstraps.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import it.buzz.tahc.bootstraps.TahcBootstrap;
import it.buzz.tahc.bootstraps.TahcScheduler;
import it.buzz.tahc.bootstraps.velocity.commands.VelocityTahcCommands;
import it.buzz.tahc.bootstraps.velocity.scheduler.VelocityTahcScheduler;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.commands.exception.CustomExceptionAdapter;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;
import revxrsal.commands.velocity.VelocityCommandHandler;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

@Plugin(
        id = "tahc",
        name = "tahc",
        authors = "Buzz",
        version = "1.0-BETA"
)
public class TahcVelocityBootstrap implements TahcBootstrap {

    private final Logger logger;
    @Getter private final ProxyServer server;
    private final Path dataDirectory;
    private final Metrics.Factory metricsFactory;

    private TahcScheduler scheduler;
    @Getter private Tahc tahc;
    private VelocityCommandHandler handler;

    @Inject
    public TahcVelocityBootstrap(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        scheduler = new VelocityTahcScheduler(this);

        tahc = new Tahc(this);
        if (!tahc.enable()){
            return;
        }

        setupCommands();

        //bstats
        metricsFactory.make(this, 21405);

        //server.getEventManager().register(this, this);
        logger.info("Enabled Tahc for Velocity Platform");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        tahc.disable();

        logger.info("Disabled Tahc for Velocity Platform");
    }

    @Override
    public Collection<Audience> getAudibleAudience() {
        return server.getAllPlayers().stream().filter(player ->
                player.hasPermission(tahc.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.ALERT_PERMISSION)))
                .collect(Collectors.toSet());
    }

    @Override
    public void log(String log) {
        logger.info(log);
    }

    @Override
    public TahcScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public File getBootstrapDataFolder() {
        return dataDirectory.toFile();
    }

    @Override
    public void executeCommand(String s) {
        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), s);
    }

    private void setupCommands() {
        handler = VelocityCommandHandler.create(getServer());
        handler.setExceptionHandler(new CustomExceptionAdapter(tahc));

        handler.register(new VelocityTahcCommands(this));
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerChat(PlayerChatEvent event) {
        if (!event.getResult().isAllowed()) return;
        Player player = event.getPlayer();

        if (tahc.getSlowdownService().checkPlayer(player, player.hasPermission("tahc.bypass.slowdown"))){
            event.setResult(PlayerChatEvent.ChatResult.denied());
            player.sendMessage(tahc.getConfigurationService().getAdaptedString(PluginConfiguration.SLOWDOWN_MESSAGE));

            return;
        }

        if (tahc.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.BYPASS_PERMISSION) && player.hasPermission("tahc.bypass"))
            return;

        String message = event.getMessage().trim();
        if (message.isBlank())
            return;

        final CheckResult checkResult = tahc.getCheckService().check(player.getUsername(), message);
        if (checkResult == CheckResult.DENIED)
            event.setResult(PlayerChatEvent.ChatResult.denied());
    }

}
