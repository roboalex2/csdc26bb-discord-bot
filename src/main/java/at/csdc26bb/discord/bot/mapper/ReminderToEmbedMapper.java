package at.csdc26bb.discord.bot.mapper;

import at.csdc26bb.discord.bot.model.Reminder;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.*;

@Slf4j
@Component
public class ReminderToEmbedMapper {

    public MessageEmbed mapToEmbed(Reminder reminder) {
        try {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Reminder: " + reminder.getTitel());
            eb.setColor(new Color(255, 0, 54));
            eb.setDescription(reminder.getDescription());
            eb.setFooter(reminder.getRemindAt().toString());
            return eb.build();
        } catch (Exception exception) {
            log.warn("Failed to build Embed for DC message for reminder: {}", reminder);
            log.warn("Detail exception: ", exception);
            return new EmbedBuilder()
                    .setTitle("There was an Exception")
                    .setDescription("Failed to build embed for reminder:\n '" + reminder + "' \nException: " + exception.getMessage())
                    .build();
        }
    }
}
