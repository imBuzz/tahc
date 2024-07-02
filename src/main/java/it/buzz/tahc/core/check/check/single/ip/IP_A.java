package it.buzz.tahc.core.check.check.single.ip;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class IP_A extends IPCheck {

    //192.168.1.1
    private final static Pattern PATTERN = Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");

    public IP_A(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("IP_A");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings for IP_A check, please check out your config.yml and be sure that IP_A is written in uppercase");
            setting = new CheckSetting(true, true, true, 10, "You sent a blocked IP", new ArrayList<>());
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
