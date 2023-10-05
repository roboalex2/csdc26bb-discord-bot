package at.csdc26bb.discord.bot.listener;

import at.csdc26bb.discord.bot.config.discord.SlashCommands;
import at.csdc26bb.discord.bot.persistence.ReminderRepository;
import at.csdc26bb.discord.bot.service.commands.ReminderCommandService;
import at.csdc26bb.discord.bot.service.commands.ReminderManagerCommandService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SlashCommandListener extends ListenerAdapter {

    private final ReminderManagerCommandService reminderManagerCommandService;
    private final ReminderRepository reminderRepository;
    private final ReminderCommandService reminderCommandService;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName().toLowerCase();

        switch (commandName) {
            case SlashCommands.REMINDER_MANAGER:
                reminderManagerCommandService.processCommand(event);
                break;
            case SlashCommands.REMINDER:
                reminderCommandService.processCommand(event);
                break;
            default:
                break;
        }

    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String optionName = event.getFocusedOption().getName().toLowerCase();
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }

        // TODO refactor into reminderManagerCommandService, reminderCommandService
        if ("role".equals(optionName)) { // TODO depending if it is the add, remove or create subcommand filter roles.
            event.replyChoices(guild.getRoles().stream()
                            .map(role -> new Command.Choice(role.getName(), role.getName()))
                            .limit(25)
                            .collect(Collectors.toList()))
                    .queue();
        } else if ("id".equals(optionName)) {
            event.replyChoices(reminderRepository.findAllByActiveIsTrue().stream()
                            .filter(el -> el.getGuildId() == guild.getIdLong())
                            .map(reminder -> new Command.Choice(reminder.getId() + " | " + reminder.getTitel(), reminder.getId()))
                            .limit(25)
                            .collect(Collectors.toList()))
                    .queue();
        }
    }
}
