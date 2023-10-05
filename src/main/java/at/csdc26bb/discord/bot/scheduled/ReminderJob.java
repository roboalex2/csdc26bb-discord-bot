package at.csdc26bb.discord.bot.scheduled;

import at.csdc26bb.discord.bot.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class ReminderJob {

    private final ReminderService reminderService;

    @Scheduled(cron = "1 * * * * *", zone = "Europe/Vienna")
    public void processReminders() {
        reminderService.processReminders();
    }
}
