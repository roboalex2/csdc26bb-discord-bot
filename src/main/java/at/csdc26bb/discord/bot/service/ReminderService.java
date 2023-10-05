package at.csdc26bb.discord.bot.service;

import at.csdc26bb.discord.bot.mapper.ReminderMapper;
import at.csdc26bb.discord.bot.model.Reminder;
import at.csdc26bb.discord.bot.persistence.ReminderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;
    private final DiscordUserService discordUserService;
    private final ReminderPublishingService reminderPublishingService;


    @Transactional
    public void processReminders() {
        OffsetDateTime currentTimestamp = Instant.now().atOffset(ZoneOffset.UTC);
        List<Reminder> relevantReminders = reminderMapper.map(
                reminderRepository.findAllByActiveIsTrueAndRemindAtIsBefore(currentTimestamp)
        );

        relevantReminders.forEach(this::publishReminderMessage);

        relevantReminders.forEach(this::markReminderAsInactive);
    }

    private void publishReminderMessage(Reminder reminder) {
        if (reminder.getRemindAt().isBefore(Instant.now().atOffset(ZoneOffset.UTC))) {
            List<User> usersByRole = discordUserService.getUsersByRole(reminder.getGuildId(), reminder.getReceiverRole());
            usersByRole.forEach(user -> reminderPublishingService.sendReminderMessage(user, reminder));
        }
    }

    private void markReminderAsInactive(Reminder reminder) {
        reminder.setActive(false);
        reminderRepository.save(reminderMapper.map(reminder));
    }

}
