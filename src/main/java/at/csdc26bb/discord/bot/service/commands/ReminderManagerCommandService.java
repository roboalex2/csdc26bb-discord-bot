package at.csdc26bb.discord.bot.service.commands;

import at.csdc26bb.discord.bot.persistence.ReminderManagerRepository;
import at.csdc26bb.discord.bot.persistence.model.ReminderManagerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderManagerCommandService {

    private final ReminderManagerRepository reminderManagerRepository;

    public void processCommand(SlashCommandInteractionEvent event) {
        boolean hashAdminPermission = Optional.ofNullable(event.getMember())
                .map(mem -> mem.hasPermission(Permission.ADMINISTRATOR))
                .orElse(false);
        if (!hashAdminPermission) {
            return;
        }

        String subcommand = event.getSubcommandName();
        if (subcommand == null || event.getUser() == null || event.getGuild() == null) {
            event.reply("Befehl, Nutzer oder Gilde nicht gefunden.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        event.deferReply() // Tell discord that a reply will come later.
                .setEphemeral(true) // Make it so the response is only seen by the sender
                .queue();

        switch (subcommand) {
            case "add":
                addManagementRole(event);
                break;
            case "remove":
                removeManagementRole(event);
                break;
            case "list":
            default:
                listManagementRoles(event);
                break;
        }
    }

    private void addManagementRole(SlashCommandInteractionEvent event) {
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

        Optional<ReminderManagerEntity> byGuildIdAndRole = reminderManagerRepository.findByGuildIdAndRole(event.getGuild().getIdLong(), role.getName());
        if (byGuildIdAndRole.isPresent()) {
            event.getHook().sendMessage(String.format(
                    "Die Rolle `%s` verwaltet bereits Reminder.",
                    role.getName()
            )).queue();
            return;
        }

        reminderManagerRepository.save(ReminderManagerEntity.builder()
                .guildId(event.getGuild().getIdLong())
                .role(role.getName())
                .build()
        );
        event.getHook().sendMessage(String.format(
                "Die Rolle `%s` kann nun den `/reminder` Befehl verwenden. :smile:",
                role.getName()
        )).queue();
    }

    private void removeManagementRole(SlashCommandInteractionEvent event) {
        Role role = Optional.ofNullable(event.getOption("role"))
                .map(OptionMapping::getAsRole)
                .orElse(null);

        Optional<ReminderManagerEntity> byGuildIdAndRole = reminderManagerRepository.findByGuildIdAndRole(event.getGuild().getIdLong(), role.getName());
        if (!byGuildIdAndRole.isPresent()) {
            event.getHook().sendMessage(String.format(
                    "Die Rolle `%s` ist nicht als Verwalter hinterlegt.",
                    role
            )).queue();
            return;
        }

        reminderManagerRepository.deleteByGuildIdAndRole(event.getGuild().getIdLong(), role.getName());
        event.getHook().sendMessage(String.format(
                "Die Rolle `%s` kann nun den `/reminder` Befehl nicht mehr verwenden. :pleading_face:",
                role.getName()
        )).queue();
    }

    private void listManagementRoles(SlashCommandInteractionEvent event) {
        List<ReminderManagerEntity> byGuildId = reminderManagerRepository.findAllByGuildId(event.getGuild().getIdLong());
        if (byGuildId.isEmpty()) {
            event.getHook().sendMessage("Es gibt aktuell keine Rolle die `/reminder` verwenden darf.").queue();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Die folgenden Rollen können aktuell `/reminder` verwenden: \n```");
        byGuildId.forEach(el -> stringBuilder.append(el.getRole() + "\n"));
        stringBuilder.append("```");

        event.getHook().sendMessage(stringBuilder.toString()).queue();
    }
}
