package it.buzz.tahc.core.check.check.multi.words;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.check.single.words.WordCheck;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.MultiWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;
import java.util.Locale;

public class BadWordsMultiCheck extends WordCheck {

    public BadWordsMultiCheck(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("MULTI_BWORDS");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings for MULTI_BWORDS check, please check out your config.yml and be sure that MULTI_BWORDS is written in uppercase");
            setting = new CheckSetting(true, true, true, 10, "You sent a blocked word", new ArrayList<>());
        }
    }

    @Override
    public CheckResult process(String message) {
        String newMessage = makeUsable(message);

        for (String string : plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.MULTI_BAD_WORDS).keySet()) {
            if (newMessage.contains(string.toLowerCase(Locale.ROOT)))
                return CheckResult.DENIED;
        }

        return CheckResult.ALLOWED;
    }

    @Override
    public Class<? extends CheckProcessor<?>> getProcessor() {
        return MultiWordProcessor.class;
    }

}
