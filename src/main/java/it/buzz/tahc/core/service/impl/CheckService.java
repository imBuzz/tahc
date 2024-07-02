package it.buzz.tahc.core.service.impl;

import com.google.common.collect.Lists;
import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.check.processor.impl.MultiWordProcessor;
import it.buzz.tahc.core.check.processor.impl.SingleWordProcessor;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import it.buzz.tahc.core.service.PluginService;
import it.buzz.tahc.core.util.Pair;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CheckService extends PluginService<Tahc> {

    private final Map<String, ViolationData> violations = new ConcurrentHashMap<>();
    @Getter private final List<CheckProcessor<?>> processors = new ArrayList<>();

    public CheckService(Tahc plugin) {
        super(plugin);
    }

    @Override
    public void start() {
        processors.addAll(
                Lists.newArrayList(
                        new MultiWordProcessor(plugin).setupChecks(),
                        new SingleWordProcessor(plugin).setupChecks()
                )
        );

        plugin.getBootstrap().getScheduler().laterTimer(() -> {
            if (violations.isEmpty())
                return;

            violations.clear();

            String string = plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.ANNOUNCE_CLEAR_VL);
            if (string.isEmpty() || string.isBlank())
                return;

            for (Audience audience : plugin.getBootstrap().getAudibleAudience())
                audience.sendMessage(plugin.getConfigurationService().getAdaptedString(string));

        }, 1L, TimeUnit.SECONDS.toMillis(10));
    }

    @Override
    public void stop() {

    }

    public CheckResult check(String player, String sentence){
        for (CheckProcessor<?> processor : processors) {
            CheckResult result = processor.handle(player, sentence);
            if (result != CheckResult.ALLOWED)
                return result;
        }

        return CheckResult.ALLOWED;
    }

    public void applyPunish(String player, Check check){
        violations.putIfAbsent(player, new ViolationData());

        ViolationData violationData = violations.get(player);
        if (violationData.getViolation(check.getClass()) >= check.getSetting().getMaxVL()) {
            violationData.clear(check.getClass());

            if (check.getSetting().isPunish())
                for (String s : check.getSetting().getCommandsOnVL())
                    plugin.getBootstrap().executeCommand(s);
        }

        violationData.registerViolation(check.getClass());
    }
    public void sendAlert(Class<? extends Check> flaggedCheck, CheckResult result, String player, String sentence, int maxVL){
        for (Audience audience : plugin.getBootstrap().getAudibleAudience()) {
            audience.sendMessage(
                    plugin.getConfigurationService().getAdaptedString(PluginConfiguration.ALERT_FORMAT,
                            new Pair<>("%action%", result.name()),
                            new Pair<>("%sender%", player),
                            new Pair<>("%check%", flaggedCheck.getSimpleName()),
                            new Pair<>("%vl%", String.valueOf(violations.get(player).getViolation(flaggedCheck))),
                            new Pair<>("%max-vl%", String.valueOf(maxVL)),
                            new Pair<>("%message%", sentence)
                    )
            );
        }
    }

    public static class ViolationData {

        private final Map<Class<? extends Check>, Integer> violations = new HashMap<>();

        public void registerViolation(Class<? extends Check> clazz){
            if (violations.containsKey(clazz)){
                violations.replace(clazz, violations.get(clazz) + 1);
            }
            else {
                violations.put(clazz, 1);
            }
        }

        public int getViolation(Class<? extends Check> clazz){
            return violations.getOrDefault(clazz, 0);
        }
        public void clear(Class<? extends Check> clazz){
            violations.remove(clazz);
        }

    }

}
