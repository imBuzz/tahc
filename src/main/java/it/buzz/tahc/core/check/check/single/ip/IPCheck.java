package it.buzz.tahc.core.check.check.single.ip;

import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;

public abstract class IPCheck extends Check {

    public IPCheck(Tahc plugin) {
        super(plugin);
    }

    protected String makeUsable(String string){
        return string
                .replaceAll("(.)\\1+", "$1")
                .replace("/", ".")
                .replace("-", ".")
                .replace(":", ".")
                .replace("(dot)", ".");
    }

}
