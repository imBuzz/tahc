package it.buzz.tahc.bootstraps.bungee.commands;

import it.buzz.tahc.bootstraps.bungee.TahcBungeeBootstrap;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;

@RequiredArgsConstructor
@Command("tahc")
public class BungeeTahcCommands {

    private final TahcBungeeBootstrap bootstrap;

    @Subcommand("reload")
    @CommandPermission("tahc.reload")
    private void onReload(BungeeCommandActor actor){
        bootstrap.getTahc().getCommands().reloadCommand(bootstrap.getAdventure().sender(actor.getSender()));
    }

}
