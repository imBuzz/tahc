package it.buzz.tahc.core.service.impl;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.properties.Property;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.service.PluginService;
import it.buzz.tahc.core.util.Pair;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;

public class ConfigurationService extends PluginService<Tahc> {

    @Getter private SettingsManager configuration;

    public ConfigurationService(Tahc plugin) {
        super(plugin);
    }

    @Override
    public void start() {
        configuration = SettingsManagerBuilder
                .withYamlFile(new File(plugin.getBootstrap().getBootstrapDataFolder().getAbsolutePath() + File.separator + "config.yml"))
                .configurationData(PluginConfiguration.class)
                .useDefaultMigrationService()
                .create();
    }

    @Override
    public void stop() {

    }

    @SafeVarargs
    public final Component getAdaptedString(Property<String> stringProperty, Pair<String, String>... replacements){
        return getAdaptedString(configuration.getProperty(stringProperty),
                replacements);
    }

    @SafeVarargs
    public final Component getAdaptedString(String finalString, Pair<String, String>... replacements){
        finalString = finalString.replace("%prefix%", plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.PREFIX));

        for (Pair<String, String> replacement : replacements)
            finalString = finalString.replace(replacement.ob1, replacement.ob2);

        return Component.empty().append(LegacyComponentSerializer.legacyAmpersand().deserialize(finalString));
    }

}
