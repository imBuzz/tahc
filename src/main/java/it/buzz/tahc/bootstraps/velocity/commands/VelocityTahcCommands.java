package it.buzz.tahc.bootstraps.velocity.commands;

import it.buzz.tahc.bootstraps.velocity.TahcVelocityBootstrap;
import lombok.RequiredArgsConstructor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.velocity.VelocityCommandActor;
import revxrsal.commands.velocity.annotation.CommandPermission;

@RequiredArgsConstructor
@Command("tahc")
public class VelocityTahcCommands {

    private final TahcVelocityBootstrap bootstrap;

    @Subcommand("reload")
    @CommandPermission("tahc.reload")
    private void onReload(VelocityCommandActor actor){
        bootstrap.getTahc().getCommands().reloadCommand(actor.getSource());
    }

}
