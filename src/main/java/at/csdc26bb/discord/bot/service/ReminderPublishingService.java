package at.csdc26bb.discord.bot.service;

import at.csdc26bb.discord.bot.mapper.ReminderToEmbedMapper;
import at.csdc26bb.discord.bot.model.Reminder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderPublishingService {

    private final ReminderToEmbedMapper reminderToEmbedMapper;

    public void sendReminderMessage(User user, Reminder reminder) {
        user.openPrivateChannel().flatMap(privateChannel ->
                privateChannel.sendMessageEmbeds(reminderToEmbedMapper.mapToEmbed(reminder))
        ).queue();
    }
}
