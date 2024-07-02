package it.buzz.tahc.core.check.processor.impl;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;
import it.buzz.tahc.core.check.check.single.domain.Domain_A;
import it.buzz.tahc.core.check.check.single.ip.IP_A;
import it.buzz.tahc.core.check.check.single.ip.IP_B;
import it.buzz.tahc.core.check.check.single.ip.IP_C;
import it.buzz.tahc.core.check.check.single.ip.IP_D;
import it.buzz.tahc.core.check.check.single.ip.IP_E;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.util.Pair;

public class SingleWordProcessor extends CheckProcessor<String[]> {

    public SingleWordProcessor(Tahc plugin) {
        super(plugin);
    }

    @Override
    public SingleWordProcessor setupChecks() {
        checks.put(Domain_A.class, new Domain_A(plugin));
        checks.put(IP_A.class, new IP_A(plugin));
        checks.put(IP_B.class, new IP_B(plugin));
        checks.put(IP_C.class, new IP_C(plugin));
        checks.put(IP_D.class, new IP_D(plugin));
        checks.put(IP_E.class, new IP_E(plugin));

        return this;
    }

    @Override
    public CheckResult process(String player, String original, String[] adapted) {
        for (String sentence : adapted) {
            sentence = sentence.toLowerCase();

            for (Check check : getChecks()) {
                CheckResult result = check.process(sentence);
                if (result == CheckResult.ALLOWED)
                    continue;

                plugin.getCheckService().applyPunish(player, check);
                plugin.getCheckService().sendAlert(check.getClass(), result, player, sentence, check.getSetting().getMaxVL());

                return result;
            }
        }

        return CheckResult.ALLOWED;
    }

    @Override
    public String[] adapt(String sentence) {
        return sentence
                .replace("_", " ")
                .split(" ");
    }

}
