package at.csdc26bb.discord.bot.persistence;

import at.csdc26bb.discord.bot.persistence.model.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    List<ReminderEntity> findAllByActiveIsTrueAndRemindAtIsBefore(OffsetDateTime currentTimestamp);
    List<ReminderEntity> findAllByActiveIsTrue();

    Optional<ReminderEntity> findById(long id);
}
