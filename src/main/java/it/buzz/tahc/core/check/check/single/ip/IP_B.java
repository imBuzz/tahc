package it.buzz.tahc.core.check.check.single.ip;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class IP_B extends IPCheck {

    //192.168.1.1:25565
    //192.168.143.143:25565

    private final static Pattern PATTERN = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.\\d{1,5}$");

    public IP_B(Tahc plugin) {
        super(plugin);
        try {
            setting = plugin.getConfigurationService().getConfiguration()
                    .getProperty(PluginConfiguration.CHECKS).get("IP_B");
        }
        catch (NullPointerException e){
            plugin.getBootstrap().log("Could not load settings for IP_B check, please check out your config.yml and be sure that IP_B is written in uppercase");
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
