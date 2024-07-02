package it.buzz.tahc.core.check.check.single.words;

import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.check.Check;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class WordCheck extends Check {

    //Used to avoid 53550 aka SESSO
    private final static Pattern PATTERN = Pattern.compile("[4831057]");
    private static final Map<String, String> SUBSTITUTIONS = new HashMap<>();
    static {
        SUBSTITUTIONS.put("4", "A");
        SUBSTITUTIONS.put("8", "B");
        SUBSTITUTIONS.put("3", "E");
        SUBSTITUTIONS.put("1", "I");
        SUBSTITUTIONS.put("0", "O");
        SUBSTITUTIONS.put("5", "S");
        SUBSTITUTIONS.put("7", "T");
    }

    public WordCheck(Tahc plugin) {
        super(plugin);
    }

    public static String makeUsable(String string){
        return PATTERN.matcher(string).replaceAll(matchResult -> SUBSTITUTIONS.get(matchResult.group()));
    }

}
