package it.buzz.tahc.core.check.check.single.domain;

import com.google.common.collect.Sets;
import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class Domain_A extends Check {

    private final static Set<String> URLS = Sets.newHashSet(
            "play.",
            "mc.",
            "playmc.",
            "mine.",
            "playminecraft.",
            "craft.",
            "server.",
            "mcserv.",
            "playserver.",
            "games.",
            "m.",
            "game.",
            "www.",
            "(dot)",
            "http:",
            "https:",

            ".eu",
            ".gg",
            ".com",
            ".co.uk",
            ".net",
            ".tk",
            ".cc",
            ".org",
            ".ly",
            ".it"
    );

    public Domain_A(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("DOMAIN_A");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings DOMAIN_A check, please check out your config.yml and be sure that DOMAIN_A is written in uppercase");
            setting = new CheckSetting(true, true, true, 10, "You sent a blocked IP", new ArrayList<>());
        }
    }

    @Override
    public CheckResult process(String message) {
        final String workingMessage = message
                .replace("/", ".")
                .replace("-", ".")
                .replace(":", ".")
                .replace("(dot)", ".");

        for (String url : URLS) {
            if ((workingMessage.startsWith(url) || workingMessage.endsWith(url))) {
                return CheckResult.DENIED;
            }
        }

        return CheckResult.ALLOWED;
    }

    @Override
    public Class<? extends CheckProcessor<?>> getProcessor() {
        return SingleWordProcessor.class;
    }

}
