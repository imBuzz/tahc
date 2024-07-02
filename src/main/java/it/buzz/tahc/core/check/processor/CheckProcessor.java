package it.buzz.tahc.core.check.processor;

import it.buzz.tahc.core.check.CheckResult;
import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;
import it.buzz.tahc.core.util.Pair;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class CheckProcessor<T> {

    protected final Tahc plugin;
    protected final Map<Class<? extends Check>, Check> checks = new LinkedHashMap<>();

    public CheckResult handle(String player, String sentence){
        return process(player, sentence, adapt(sentence));
    }

    public abstract CheckProcessor<T> setupChecks();
    protected abstract CheckResult process(String player, String original, T adapted);
    protected abstract T adapt(String sentence);

    protected Collection<Check> getChecks(){
        return checks.values();
    }

}
