package at.csdc26bb.discord.bot.config.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class SlashCommandConfig {

    private final JDA jdaInstance;
    private final List<ListenerAdapter> eventListeners;

    @EventListener(ApplicationReadyEvent.class)
    public void configureRuntime() {
        jdaInstance.addEventListener(eventListeners.toArray());

        jdaInstance.updateCommands().addCommands(
                Commands.slash(SlashCommands.REMINDER_MANAGER, "Add/List/Remove a role capable of reminder management")
                        .setGuildOnly(true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                        .addSubcommands(
                                new SubcommandData("add", "Add a role capable of reminder management")
                                        .addOptions(
                                                new OptionData(OptionType.ROLE, "role", "The role that should be able to manage reminders")
                                                        .setRequired(true)
                                        ),
                                new SubcommandData("remove", "Remove a role capable of reminder management")
                                        .addOptions(
                                                new OptionData(OptionType.ROLE, "role", "The role that should no longer be able to manage reminders")
                                                        .setRequired(true)
                                        ),
                                new SubcommandData("list", "List roles capable of reminder management")
                        ),
                Commands.slash(SlashCommands.REMINDER, "Create/List/Remove a new reminder")
                        .setGuildOnly(true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.EMPTY_PERMISSIONS))
                        .addSubcommands(
                                new SubcommandData("create", "Create a new reminder")
                                        .addOptions(
                                                new OptionData(OptionType.ROLE, "role", "The role that will receive the reminder")
                                                        .setRequired(true)
                                        )
                                        .addOptions(
                                                new OptionData(OptionType.STRING, "timestamp", "The ISO timestamp when people should be reminded")
                                                        .setRequired(true)
                                        )
                                        .addOptions(
                                                new OptionData(OptionType.STRING, "title", "The title of the reminder")
                                                        .setRequired(true)
                                        )
                                        .addOptions(
                                                new OptionData(OptionType.STRING, "description", "Additional information to the reminder")
                                                        .setRequired(false)
                                        ),
                                new SubcommandData("list", "List all active reminders"),
                                new SubcommandData("abort", "Remove a role capable of reminder management")
                                        .addOptions(
                                                new OptionData(OptionType.INTEGER, "id", "The ID of the reminder that should be aborted")
                                                        .setRequired(true).setAutoComplete(true)
                                        )

                        )
        ).queue();
    }
}
