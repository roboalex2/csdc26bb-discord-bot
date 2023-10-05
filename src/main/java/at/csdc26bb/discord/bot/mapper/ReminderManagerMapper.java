package at.csdc26bb.discord.bot.mapper;

import at.csdc26bb.discord.bot.model.ReminderManager;
import at.csdc26bb.discord.bot.persistence.model.ReminderManagerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReminderManagerMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReminderManagerEntity map(ReminderManager reminderManager);
    ReminderManager map(ReminderManagerEntity reminderManagerEntity);
}
