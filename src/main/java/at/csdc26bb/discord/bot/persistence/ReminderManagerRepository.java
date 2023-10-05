package at.csdc26bb.discord.bot.persistence;

import at.csdc26bb.discord.bot.persistence.model.ReminderManagerEntity;
import at.csdc26bb.discord.bot.persistence.model.ReminderManagerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderManagerRepository extends JpaRepository<ReminderManagerEntity, ReminderManagerId> {
    List<ReminderManagerEntity> findAllByGuildId(long guildId);
    void deleteByGuildIdAndRole(long guildId, String role);
    Optional<ReminderManagerEntity> findByGuildIdAndRole(long guildId, String role);
}
