package it.buzz.tahc.core.check.check.single.words.impl;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.check.single.words.WordCheck;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SpecialCharacters extends WordCheck {

    private static final Pattern PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    public SpecialCharacters(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("SPECIAL_CHARACTERS");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings for SPECIAL_CHARACTERS check, please check out your config.yml and be sure that SPECIAL_CHARACTERS is written in uppercase");
            setting = new CheckSetting(true, true, true, 10, "You sent a blocked character", new ArrayList<>());
        }
    }

    @Override
    public CheckResult process(String message) {
        return PATTERN.matcher(makeUsable(message)).matches() ? CheckResult.DENIED : CheckResult.ALLOWED;
    }

    @Override
    public Class<? extends CheckProcessor<?>> getProcessor() {
        return SingleWordProcessor.class;
    }
}
