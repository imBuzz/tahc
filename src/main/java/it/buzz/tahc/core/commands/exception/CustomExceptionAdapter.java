package it.buzz.tahc.core.commands.exception;

import it.buzz.tahc.core.Tahc;
import it.buzz.tahc.core.configuration.PluginConfiguration;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.BukkitExceptionAdapter;
import revxrsal.commands.bukkit.exception.InvalidPlayerException;
import revxrsal.commands.bukkit.exception.InvalidWorldException;
import revxrsal.commands.bukkit.exception.MalformedEntitySelectorException;
import revxrsal.commands.bukkit.exception.MoreThanOnePlayerException;
import revxrsal.commands.bukkit.exception.NonPlayerEntitiesException;
import revxrsal.commands.bukkit.exception.SenderNotConsoleException;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.exception.ArgumentParseException;
import revxrsal.commands.exception.CommandInvocationException;
import revxrsal.commands.exception.CooldownException;
import revxrsal.commands.exception.EnumNotFoundException;
import revxrsal.commands.exception.InvalidBooleanException;
import revxrsal.commands.exception.InvalidCommandException;
import revxrsal.commands.exception.InvalidHelpPageException;
import revxrsal.commands.exception.InvalidNumberException;
import revxrsal.commands.exception.InvalidSubcommandException;
import revxrsal.commands.exception.InvalidURLException;
import revxrsal.commands.exception.InvalidUUIDException;
import revxrsal.commands.exception.MissingArgumentException;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.exception.NoSubcommandSpecifiedException;
import revxrsal.commands.exception.NumberNotInRangeException;
import revxrsal.commands.exception.SendableException;
import revxrsal.commands.exception.TooManyArgumentsException;
import revxrsal.commands.util.Strings;

import java.text.MessageFormat;

public class CustomExceptionAdapter extends BukkitExceptionAdapter {

    private final Tahc plugin;

    public CustomExceptionAdapter(Tahc plugin) {
        this.plugin = plugin;
    }

    @Override
    public void missingArgument(CommandActor actor, MissingArgumentException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.MISSING_ARGUMENT), exception.getParameter().getName())));
    }

    @Override
    public void invalidEnumValue(CommandActor actor, EnumNotFoundException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_ENUM), exception.getParameter().getName(), exception.getInput())));
    }

    @Override
    public void invalidNumber(CommandActor actor, InvalidNumberException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_NUMBER), exception.getInput())));
    }

    @Override
    public void invalidUUID(CommandActor actor, InvalidUUIDException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_UUID), exception.getInput())));
    }

    @Override
    public void invalidURL(CommandActor actor, InvalidURLException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_URL), exception.getInput())));
    }

    @Override
    public void invalidBoolean(CommandActor actor, InvalidBooleanException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_BOOLEAN), exception.getInput())));
    }

    @Override
    public void noPermission(CommandActor actor, NoPermissionException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.NO_PERMISSION)));
    }

    @Override
    public void argumentParse(CommandActor actor, ArgumentParseException exception) {
        actor.as(BukkitCommandActor.class).getSender().sendMessage(Strings.colorize(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_QUOTED_STRING)));
        actor.error(exception.getSourceString());
        actor.error(exception.getAnnotatedPosition());
    }

    @Override
    public void commandInvocation(CommandActor actor, CommandInvocationException exception) {
        actor.as(BukkitCommandActor.class).getSender().sendMessage(Strings.colorize(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.ERROR_OCCURRED)));
        exception.getCause().printStackTrace();
    }

    @Override
    public void tooManyArguments(CommandActor actor, TooManyArgumentsException exception) {
        ExecutableCommand command = exception.getCommand();
        String usage = (command.getPath().toRealString() + " " + command.getUsage()).trim();

        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.TOO_MANY_ARGUMENTS), usage)));
    }

    @Override
    public void invalidCommand(CommandActor actor, InvalidCommandException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_COMMAND), exception.getInput())));
    }

    @Override
    public void invalidSubcommand(CommandActor actor, InvalidSubcommandException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_SUBCOMMAND), exception.getInput())));
    }

    @Override
    public void noSubcommandSpecified(CommandActor actor, NoSubcommandSpecifiedException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.NO_SUBCOMMAND_SPECIFIED)));
    }

    @Override
    public void cooldown(CommandActor actor, CooldownException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.ON_COOLDOWN), formatTimeFancy(exception.getTimeLeftMillis()))));
    }

    @Override
    public void invalidHelpPage(CommandActor actor, InvalidHelpPageException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_HELP_PAGE), exception.getPage(), exception.getPageCount())));
    }

    @Override
    public void sendableException(CommandActor actor, SendableException exception) {
        exception.sendTo(actor);
    }

    @Override
    public void numberNotInRange(CommandActor actor, NumberNotInRangeException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.NUMBER_NOT_IN_RANGE), exception.getParameter().getName(),
                        FORMAT.format(exception.getMinimum()), FORMAT.format(exception.getMaximum()), FORMAT.format(exception.getInput()))));
    }

    @Override
    public void senderNotPlayer(CommandActor actor, SenderNotPlayerException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.MUST_BE_PLAYER)));
    }

    @Override
    public void senderNotConsole(CommandActor actor, SenderNotConsoleException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.MUST_BE_CONSOLE)));
    }

    @Override
    public void invalidPlayer(CommandActor actor, InvalidPlayerException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_PLAYER), exception.getInput())));
    }

    @Override
    public void invalidWorld(CommandActor actor, InvalidWorldException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_WORLD), exception.getInput())));
    }

    @Override
    public void malformedEntitySelector(CommandActor actor, MalformedEntitySelectorException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.INVALID_SELECTOR), exception.getInput())));
    }

    @Override
    public void moreThanOnePlayer(CommandActor actor, MoreThanOnePlayerException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.ONLY_ONE_PLAYER), exception.getInput())));
    }

    @Override
    public void nonPlayerEntities(CommandActor actor, NonPlayerEntitiesException exception) {
        actor.as(BukkitCommandActor.class).getSender()
                .sendMessage(Strings.colorize(MessageFormat.format(plugin.getConfigurationService().getConfiguration().getProperty(PluginConfiguration.NON_PLAYERS_NOT_ALLOWED), exception.getInput())));
    }

}
