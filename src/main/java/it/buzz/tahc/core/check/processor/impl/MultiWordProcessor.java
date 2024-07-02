package it.buzz.tahc.core.check.processor.impl;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;
import it.buzz.tahc.core.check.check.multi.words.BadWordsMultiCheck;
import it.buzz.tahc.core.check.check.single.words.WordCheck;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.util.Pair;

public class MultiWordProcessor extends CheckProcessor<String> {

    public MultiWordProcessor(Tahc plugin) {
        super(plugin);
    }

    @Override
    public MultiWordProcessor setupChecks() {
        checks.put(BadWordsMultiCheck.class, new BadWordsMultiCheck(plugin));

        return this;
    }

    @Override
    public CheckResult process(String player, String original, String sentence) {
        for (Check check : getChecks()) {
            CheckResult result = check.process(sentence);
            if (result == CheckResult.ALLOWED)
                continue;

            plugin.getCheckService().applyPunish(player, check);
            plugin.getCheckService().sendAlert(check.getClass(), result, player, original, check.getSetting().getMaxVL());

            return result;
        }

        return CheckResult.ALLOWED;
    }

    @Override
    public String adapt(String sentence) {
        return WordCheck.makeUsable(sentence).toLowerCase();
    }

}
