package at.csdc26bb.discord.bot.mapper;

import at.csdc26bb.discord.bot.model.Reminder;
import at.csdc26bb.discord.bot.persistence.model.ReminderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ReminderMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReminderEntity map(Reminder reminder);
    List<Reminder> map(List<ReminderEntity> reminderEntity);
    Reminder map(ReminderEntity reminderEntity);
}
