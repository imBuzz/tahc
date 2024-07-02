package it.buzz.tahc.core.check;

import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.processor.CheckProcessor;
import it.buzz.tahc.core.configuration.beans.CheckSetting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Check {

    protected final Tahc plugin;
    @Getter protected CheckSetting setting;

    public abstract CheckResult process(String message);
    public abstract Class<? extends CheckProcessor<?>> getProcessor();


}
