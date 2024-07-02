package it.buzz.tahc.core.check.check.single.ip;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class IP_E extends IPCheck {

    //2001:0db8:85a3:0000:0000:8a2e:0370:7334

    private final static Pattern PATTERN = Pattern.compile("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    public IP_E(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("IP_E");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings for IP_E check, please check out your config.yml and be sure that IP_E is written in uppercase");
            setting = new CheckSetting(true, true, true, 10, "You sent a blocked IP", new ArrayList<>());
        }
    }

    @Override
    public CheckResult process(String message) {
        return PATTERN.matcher(makeUsable(message).replace(".", ":")).matches() ? CheckResult.DENIED : CheckResult.ALLOWED;
    }

    @Override
    public Class<? extends CheckProcessor<?>> getProcessor() {
        return SingleWordProcessor.class;
    }

}
