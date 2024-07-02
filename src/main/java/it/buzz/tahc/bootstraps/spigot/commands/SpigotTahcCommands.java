package it.buzz.tahc.bootstraps.spigot.commands;

import it.buzz.tahc.bootstraps.spigot.TahcSpigotBootstrap;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@RequiredArgsConstructor
@Command("tahc")
public class SpigotTahcCommands {

    private final TahcSpigotBootstrap bootstrap;

    @Subcommand("reload")
    @CommandPermission("tahc.reload")
    private void onReload(BukkitCommandActor actor){
        bootstrap.getTahc().getCommands().reloadCommand(actor.audience());
    }

}
