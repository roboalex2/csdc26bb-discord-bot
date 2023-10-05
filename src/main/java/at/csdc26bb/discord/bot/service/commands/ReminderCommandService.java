package at.csdc26bb.discord.bot.service.commands;

import at.csdc26bb.discord.bot.mapper.ReminderMapper;
import at.csdc26bb.discord.bot.model.Reminder;
import at.csdc26bb.discord.bot.persistence.ReminderManagerRepository;
import at.csdc26bb.discord.bot.persistence.ReminderRepository;
import at.csdc26bb.discord.bot.persistence.model.ReminderEntity;
import at.csdc26bb.discord.bot.persistence.model.ReminderManagerEntity;
import at.csdc26bb.discord.bot.service.OffsetDateTimeConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderCommandService {

    private final ReminderManagerRepository reminderManagerRepository;
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final OffsetDateTimeConversionService offsetDateTimeConversionService;

    public void processCommand(SlashCommandInteractionEvent event) {
        String subcommand = event.getSubcommandName();
        if (subcommand == null || event.getMember() == null || event.getGuild() == null) {
            event.reply("Befehl, Nutzer oder Gilde nicht gefunden.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        event.deferReply() // Tell discord that a reply will come later.
                .setEphemeral(true) // Make it so the response is only seen by the sender
                .queue();


        List<ReminderManagerEntity> allByGuildId = reminderManagerRepository.findAllByGuildId(event.getGuild().getIdLong());
        boolean hasPermission = allByGuildId.stream().anyMatch(
                el -> event.getMember().getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
                        .contains(el.getRole())
        );
        if (!hasPermission) {
            event.getHook()
                    .sendMessage("Du hast nicht die nötigen Rechte für diesen Befehl. Wende dich an einen Admin der Gilde.")
                    .queue();
            return;
        }

        switch (subcommand) {
            case "create":
                createReminder(event);
                break;
            case "abort":
                abortReminder(event);
                break;
            case "list":
            default:
                listReminders(event);
                break;
        }
    }

    private void createReminder(SlashCommandInteractionEvent event) {
        Role role = Optional.ofNullable(event.getOption("role"))
                .map(OptionMapping::getAsRole)
                .orElse(null);
        boolean guildHasRole = event.getGuild().getRoles().stream()
                .anyMatch(el -> el.equals(role));
        if (!guildHasRole) {
            event.getHook().sendMessage(String.format(
                    "Die Rolle `%s` wurde in dieser Gilde nicht gefunden.",
                    role.getName()
            )).queue();
            return;
        }

        Optional<String> optionalTimestamp = Optional.ofNullable(event.getOption("timestamp"))
                .map(OptionMapping::getAsString);
        OffsetDateTime timestamp = optionalTimestamp.map(offsetDateTimeConversionService::fromCharSequence)
                .orElse(null);
        if (timestamp == null) {
            event.getHook().sendMessage(String.format(
                    "Der gegebene Zeitstempel `%s` ist nicht im ISO format.\n" +
                            "Example: `2023-10-05T00:30:38Z`, `2023-10-05T00:30:38+02:00`\n" +
                            "Beachte die Zeitzone korrekt zu setzen.",
                    optionalTimestamp.orElse("INFORMATION MISSING")
            )).queue();
            return;
        } else if (timestamp.isBefore(Instant.now().atOffset(ZoneOffset.UTC))) {
            event.getHook().sendMessage(String.format(
                    "Der gegebene Zeitstempel `%s` muss in der Zukunft liegen.",
                    optionalTimestamp.orElse("INFORMATION MISSING")
            )).queue();
            return;
        }

        String title = Optional.ofNullable(event.getOption("title"))
                .map(OptionMapping::getAsString)
                .map(String::strip)
                .orElse(null);
        if (title == null) {
            event.getHook().sendMessage("Der Titel muss gesetzt werden.").queue();
            return;
        }

        String description = Optional.ofNullable(event.getOption("description"))
                .map(OptionMapping::getAsString)
                .map(String::strip)
                .orElse("");

        ReminderEntity save = reminderRepository.save(ReminderEntity.builder()
                .active(true)
                .remindAt(timestamp.withOffsetSameInstant(ZoneOffset.UTC))
                .guildId(event.getGuild().getIdLong())
                .titel(title)
                .description(description)
                .receiverRole(role.getName())
                .build()
        );
        event.getHook().sendMessage("Der Reminder wurde erfolgreich Angelegt mit:\n" +
                "```\n" +
                "ID: " + save.getId() + "\n" +
                "Titel: " + title + "\n" +
                "Beschreibung: " + description + "\n" +
                "Ziel-Rolle: " + role.getName() + "\n" +
                "Zeitpunkt: " + timestamp + "\n" +
                "```"
        ).queue();
    }

    private void abortReminder(SlashCommandInteractionEvent event) {
        Long id = Optional.ofNullable(event.getOption("id"))
                .map(OptionMapping::getAsLong)
                .orElse(null);
        if (id == null) {
            event.getHook().sendMessage("Die ID muss gesetzt werden.").queue();
            return;
        }

        ReminderEntity reminderById = reminderRepository.findById(id).orElse(null);
        if (reminderById == null || !reminderById.getActive() || reminderById.getGuildId() != event.getGuild().getIdLong()) {
            event.getHook().sendMessage(String.format(
                    "Es wurde kein Reminder mit der ID `%s` gefunden.",
                    id
            )).queue();
            return;
        }

        Reminder reminder = reminderMapper.map(reminderById);
        reminderById.setActive(false);
        reminderRepository.save(reminderById);
        event.getHook().sendMessage("Der Reminder:\n" +
                "```\n" +
                "ID: " + reminder.getId() + "\n" +
                "Titel: " + reminder.getTitel() + "\n" +
                "Beschreibung: " + reminder.getDescription() + "\n" +
                "Ziel-Rolle: " + reminder.getReceiverRole() + "\n" +
                "Zeitpunkt: " + reminder.getRemindAt() + "\n" +
                "```\n" +
                "wurde abgebrochen."
        ).queue();
    }

    private void listReminders(SlashCommandInteractionEvent event) {
        List<Reminder> localReminder = reminderRepository.findAllByActiveIsTrue().stream()
                .map(reminderMapper::map)
                .filter(remider -> remider.getGuildId() == event.getGuild().getIdLong())
                .toList();
        if (localReminder.isEmpty()) {
            event.getHook().sendMessage("Es gibt keine kommenden Reminder.").queue();
            return;
        }


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Die folgenden Reminder sind gestellt: \n```");
        localReminder.forEach(el -> stringBuilder.append(el.getId() + " | " + el.getRemindAt() + " | " + el.getReceiverRole() + " | " + el.getTitel() + "\n"));
        stringBuilder.append("```");
        event.getHook().sendMessage(stringBuilder.toString()).queue();
    }
}
