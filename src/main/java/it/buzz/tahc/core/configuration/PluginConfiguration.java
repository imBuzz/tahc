package it.buzz.tahc.core.configuration;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.beanmapper.DefaultMapper;
import ch.jalu.configme.properties.BooleanProperty;
import ch.jalu.configme.properties.IntegerProperty;
import ch.jalu.configme.properties.MapProperty;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyBuilder;
import ch.jalu.configme.properties.StringProperty;
import ch.jalu.configme.properties.types.BeanPropertyType;
import ch.jalu.configme.properties.types.PrimitivePropertyType;
import com.google.common.collect.Lists;
import it.buzz.tahc.core.configuration.beans.CheckSetting;

import java.util.LinkedHashMap;
import java.util.Map;

public class PluginConfiguration implements SettingsHolder {

    public static Property<String> PREFIX = new StringProperty("language.prefix", "[TAHC]");
    public static Property<String> SLOWDOWN_MESSAGE = new StringProperty("language.slowdown", "&cSlowdown");

    @Comment("If not blank, players with \"tahc.alerts\" will receive this message")
    public static Property<String> ANNOUNCE_CLEAR_VL = new StringProperty("language.alerts.clear-vl-announce", "&e%prefix% All the previous VL have been removed");

    public static Property<String> ALERT_PERMISSION = new StringProperty("language.alerts.permission", "tahc.alerts");
    public static Property<String> ALERT_FORMAT = new StringProperty("language.alerts.format",
            "&e%prefix% %sender% failed to send \"%message%\" %check% &7(%action%) (%vl%/%max-vl%)");

    public static final Property<String> RELOAD_MESSAGE = new StringProperty("language.commands.reload", "&aReload Completed");
    public static final Property<String> INVALID_ENUM = new StringProperty("language.commands.invalid-enum", "&cInvalid {0}: {1}.");
    public static final Property<String> INVALID_NUMBER = new StringProperty("language.commands.invalid-number", "&cExpected a number, but found {0}.");
    public static final Property<String> INVALID_UUID = new StringProperty("language.commands.invalid-uuid", "&cInvalid UUID: {0}");
    public static final Property<String> INVALID_URL = new StringProperty("language.commands.invalid-url", "&cInvalid URL: {0}");
    public static final Property<String> INVALID_BOOLEAN = new StringProperty("language.commands.invalid-boolean", "&cExpected true or false, but found {0}");
    public static final Property<String> INVALID_QUOTED_STRING = new StringProperty("language.commands.invalid-quoted-string", "&cInvalid quoted string");
    public static final Property<String> INVALID_COMMAND = new StringProperty("language.commands.invalid-command", "&cInvalid command: {0}.");
    public static final Property<String> INVALID_SUBCOMMAND = new StringProperty("language.commands.invalid-subcommand", "&cInvalid subcommand: {0}.");
    public static final Property<String> INVALID_HELP_PAGE = new StringProperty("language.commands.invalid-help-page", "&cInvalid help page: {0}. Must be between 1 and {1}.");

    public static final Property<String> MISSING_ARGUMENT = new StringProperty("language.commands.missing-argument", "&cYou must specify a value for the {0}!");
    public static final Property<String> NO_PERMISSION = new StringProperty("language.commands.no-permission", "&cYou do not have permission to execute this command.");
    public static final Property<String> ERROR_OCCURRED = new StringProperty("language.commands.error-occurred", "&cAn error occurred while executing this command.");
    public static final Property<String> TOO_MANY_ARGUMENTS = new StringProperty("language.commands.too-many-arguments", "&cToo many arguments! Correct usage: /{0}");
    public static final Property<String> NO_SUBCOMMAND_SPECIFIED = new StringProperty("language.commands.no-subcommand-specified", "&cYou must specify a subcommand!");
    public static final Property<String> ON_COOLDOWN = new StringProperty("language.commands.on-cooldown", "&cYou must wait {0} before using this command again.");
    public static final Property<String> NUMBER_NOT_IN_RANGE = new StringProperty("language.commands.number-not-in-range", "&c{0} must be between {1} and {2} (found {3})");

    public static final Property<String> MUST_BE_PLAYER = new StringProperty("language.commands.must-be-player", "&cYou must be a player to use this command!");
    public static final Property<String> MUST_BE_CONSOLE = new StringProperty("language.commands.must-be-console", "&cThis command can only be used on console!");
    public static final Property<String> INVALID_PLAYER = new StringProperty("language.commands.invalid-player", "&cInvalid player: &e{0}");
    public static final Property<String> INVALID_WORLD = new StringProperty("language.commands.invalid-world", "&cInvalid world: &e{0}");
    public static final Property<String> INVALID_SELECTOR = new StringProperty("language.commands.invalid-selector", "&cInvalid selector argument: &e{0}");
    public static final Property<String> ONLY_ONE_PLAYER = new StringProperty("language.commands.only-one-player", "&cSelector &e{0} &callows more than one player. Only one is allowed");
    public static final Property<String> NON_PLAYERS_NOT_ALLOWED = new StringProperty("language.commands.non-players-not-allowed", "&cNon-player entities are not allowed");

    @Comment("If activated, players with \"tahc.bypass\" permission will ignore checks")
    public static Property<Boolean> BYPASS_PERMISSION = new BooleanProperty("options.bypass-permission", false);

    @Comment("Change the amount of milliseconds a player should wait before chatting again, put -1 to disable")
    public static Property<Integer> SLOWDOWN_TIMING = new IntegerProperty("options.slowdown", 2000);
    @Comment("If activated, players with \"tahc.bypass.slowdown\" permission will ignore slowdown")
    public static Property<Boolean> BYPASS_SLOWDOWN_PERMISSION = new BooleanProperty("options.bypass-slowdown-permission", false);

    public static final Property<Map<String, CheckSetting>> CHECKS =
            new BeanMapProperty<>("checks",
                    BeanPropertyType.of(CheckSetting.class, DefaultMapper.getInstance()),
                    defaultChecks()
            );

    private static Map<String, CheckSetting> defaultChecks(){
        Map<String, CheckSetting> groups = new LinkedHashMap<>();

        groups.put("IP_A", new CheckSetting(true, true, false, 5, "You sent a blocked IP", Lists.newArrayList("kick %player% You sent a blocked IP")));
        groups.put("IP_B", new CheckSetting(true, true, false, 5, "You sent a blocked IP", Lists.newArrayList("kick %player% You sent a blocked IP")));
        groups.put("IP_C", new CheckSetting(true, true, false, 5, "You sent a blocked IP", Lists.newArrayList("kick %player% You sent a blocked IP")));
        groups.put("IP_D", new CheckSetting(true, true, false, 5, "You sent a blocked IP", Lists.newArrayList("kick %player% You sent a blocked IP")));
        groups.put("IP_E", new CheckSetting(true, true, false, 5, "You sent a blocked IP", Lists.newArrayList("kick %player% You sent a blocked IP")));

        groups.put("DOMAIN_A", new CheckSetting(true, true, false, 5, "You sent a blocked IP", Lists.newArrayList("kick %player% You sent a blocked domain")));

        groups.put("SPECIAL_CHARACTERS", new CheckSetting(true, true, false, 5, "You sent a blocked character", Lists.newArrayList("kick %player% You sent a blocked character")));
        groups.put("SINGLE_BWORDS", new CheckSetting(true, true, false, 5, "You sent a blocked word", Lists.newArrayList("kick %player% You sent a blocked word")));
        groups.put("MULTI_BWORDS", new CheckSetting(true, true, false, 5, "You sent a blocked word", Lists.newArrayList("kick %player% You sent a blocked word")));

        return groups;
    }

    @Comment("All of these words are CASE_INSENSITIVE - 'true' to apply the punishment for single word, 'false' to only flag")
    public static final MapProperty<Boolean> SINGLE_BAD_WORDS = new PropertyBuilder.MapPropertyBuilder<>(PrimitivePropertyType.BOOLEAN)
            .path("bad-words.single")
            .defaultEntry("Dickhead", true)
            .build();

    @Comment("All of these words are CASE_INSENSITIVE - 'true' to apply the punishment for single word, 'false' to only flag")
    public static final MapProperty<Boolean> MULTI_BAD_WORDS = new PropertyBuilder.MapPropertyBuilder<>(PrimitivePropertyType.BOOLEAN)
            .path("bad-words.multi")
            .defaultEntry("Fuck you", true)
            .build();
    
}
