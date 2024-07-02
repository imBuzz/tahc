package it.buzz.tahc.core.commands;

import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;

@RequiredArgsConstructor
public class TahcCommands {

    private final Tahc tahc;

    public void reloadCommand(Audience sender){
        tahc.getConfigurationService().getConfiguration().reload();
        sender.sendMessage(tahc.getConfigurationService().getAdaptedString(
                tahc.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.RELOAD_MESSAGE)));
    }


}
