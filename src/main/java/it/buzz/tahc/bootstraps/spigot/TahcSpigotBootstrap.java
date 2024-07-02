package it.buzz.tahc.bootstraps.spigot;

import it.buzz.tahc.bootstraps.spigot.commands.SpigotTahcCommands;
import it.buzz.tahc.bootstraps.spigot.protocol.ProtocolVersion;
import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.bootstraps.TahcScheduler;
import it.buzz.tahc.bootstraps.TahcBootstrap;
import it.buzz.tahc.bootstraps.spigot.scheduler.SpigotTahcScheduler;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.commands.exception.CustomExceptionAdapter;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.util.Pair;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.exception.CommandErrorException;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public class TahcSpigotBootstrap extends JavaPlugin implements TahcBootstrap, Listener {

    @Getter private TahcScheduler scheduler;
    private BukkitAudiences adventure;
    private BukkitCommandHandler handler;

    @Getter private Tahc tahc;

    @Override
    public void onEnable() {
        scheduler = new SpigotTahcScheduler(this);
        adventure = BukkitAudiences.create(this);

        tahc = new Tahc(this);
        if (!tahc.enable()){
            return;
        }

        setupCommands();
        Bukkit.getPluginManager().registerEvents(this, this);

        new Metrics(this, 21405);

        log("Enabled Tahc for Spigot Platform");
    }

    @Override
    public void onDisable() {
        tahc.disable();
        adventure.close();

        log("Disabled Tahc for Spigot Platform");
    }

    @Override
    public File getBootstrapDataFolder() {
        return getDataFolder();
    }

    @Override
    public Collection<Audience> getAudibleAudience() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(tahc.getConfigurationService().getConfiguration()
                        .getProperty(PluginConfiguration.ALERT_PERMISSION)))
                .map(player -> adventure.player(player))
                .collect(Collectors.toSet());
    }

    @Override
    public void log(String log) {
        getLogger().info(log);
    }

    @Override
    public void executeCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    private void setupCommands() {
        handler = BukkitCommandHandler.create(this);
        handler.enableAdventure();
        handler.setExceptionHandler(new CustomExceptionAdapter(tahc));

        handler.register(new SpigotTahcCommands(this));
        if (ProtocolVersion.getCurrentVersion().isNewer(ProtocolVersion.v1_13_R2))
            handler.registerBrigadier();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        Audience audience = adventure.player(player);

        if (tahc.getSlowdownService().checkPlayer(audience, player.hasPermission("tahc.bypass.slowdown"))){
            event.setCancelled(true);
            audience.sendMessage(tahc.getConfigurationService().getAdaptedString(PluginConfiguration.SLOWDOWN_MESSAGE));

            return;
        }

        if (tahc.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.BYPASS_PERMISSION) && player.hasPermission("tahc.bypass"))
            return;

        String message = event.getMessage().trim();
        if (message.isBlank())
            return;

        final CheckResult checkResult = tahc.getCheckService().check(player.getName(), message);
        if (checkResult == CheckResult.DENIED)
            event.setCancelled(true);
    }


}
