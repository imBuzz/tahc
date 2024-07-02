package it.buzz.tahc.core.check.check.single.words.impl;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.check.single.words.WordCheck;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;

public class SingleBWords extends WordCheck {

    public SingleBWords(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("SINGLE_BWORDS");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings for SINGLE_BWORDS check, please check out your config.yml and be sure that SINGLE_BWORDS is written in uppercase");
            setting = new CheckSetting(true, true, true, 10, "You sent a blocked word", new ArrayList<>());
        }
    }

    @Override
    public CheckResult process(String message) {
        boolean shouldBePunished = plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.SINGLE_BAD_WORDS).getOrDefault(makeUsable(message), false);
        return plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.SINGLE_BAD_WORDS)
                .containsKey(makeUsable(message)) ? shouldBePunished ? CheckResult.DENIED : CheckResult.FLAGGED : CheckResult.ALLOWED;
    }

    @Override
    public Class<? extends CheckProcessor<?>> getProcessor() {
        return SingleWordProcessor.class;
    }

}
