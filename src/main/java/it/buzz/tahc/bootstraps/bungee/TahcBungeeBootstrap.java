package it.buzz.tahc.bootstraps.bungee;

import it.buzz.tahc.bootstraps.TahcBootstrap;
import it.buzz.tahc.bootstraps.TahcScheduler;
import it.buzz.tahc.bootstraps.bungee.commands.BungeeTahcCommands;
import it.buzz.tahc.bootstraps.bungee.scheduler.BungeeTahcScheduler;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.commands.exception.CustomExceptionAdapter;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.bstats.bungeecord.Metrics;
import revxrsal.commands.bungee.BungeeCommandHandler;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public class TahcBungeeBootstrap extends Plugin implements TahcBootstrap, Listener {

    @Getter private TahcScheduler scheduler;
    @Getter private BungeeAudiences adventure;
    @Getter private Tahc tahc;

    private BungeeCommandHandler handler;

    @Override
    public void onEnable() {
        scheduler = new BungeeTahcScheduler(this);
        adventure = BungeeAudiences.create(this);

        tahc = new Tahc(this);
        if (!tahc.enable()){
            return;
        }

        setupCommands();
        getProxy().getPluginManager().registerListener(this, this);

        new Metrics(this, 21405);

        log("Enabled Tahc for Bungee Platform");
    }

    @Override
    public void onDisable() {
        tahc.disable();
        adventure.close();
    }

    @Override
    public File getBootstrapDataFolder() {
        return getProxy().getPluginsFolder();
    }

    @Override
    public Collection<Audience> getAudibleAudience() {
        return getProxy().getPlayers().stream().filter(player -> player.hasPermission(tahc.getConfigurationService().getConfiguration()
                        .getProperty(PluginConfiguration.ALERT_PERMISSION)))
                .map(player -> adventure.player(player))
                .collect(Collectors.toSet());
    }

    @Override
    public void log(String log) {
        getProxy().getLogger().info(log);
    }

    @Override
    public void executeCommand(String s) {
        getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), s);
    }

    private void setupCommands() {
        handler = BungeeCommandHandler.create(this);
        handler.setExceptionHandler(new CustomExceptionAdapter(tahc));

        handler.register(new BungeeTahcCommands(this));
    }

    @EventHandler
    public void onChatEvent(ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer){
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();
            Audience audience = adventure.player(proxiedPlayer);

            if (tahc.getSlowdownService().checkPlayer(audience, proxiedPlayer.hasPermission("tahc.bypass.slowdown"))){
                event.setCancelled(true);
                audience.sendMessage(tahc.getConfigurationService().getAdaptedString(PluginConfiguration.SLOWDOWN_MESSAGE));

                return;
            }

            if (tahc.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.BYPASS_PERMISSION) && proxiedPlayer.hasPermission("tahc.bypass"))
                return;

            String message = event.getMessage().trim();
            if (message.isBlank())
                return;

            final CheckResult checkResult = tahc.getCheckService().check(proxiedPlayer.getName(), message);
            if (checkResult == CheckResult.DENIED)
                event.setCancelled(true);
        }
    }

}
