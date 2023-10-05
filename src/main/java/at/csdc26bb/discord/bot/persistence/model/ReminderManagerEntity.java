package at.csdc26bb.discord.bot.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ReminderManagerId.class)
@Table(name = "reminder_manager")
public class ReminderManagerEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long guildId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "role", length = 64)
    private String role;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "CREATED_AT", columnDefinition = "TIMESTAMP")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UPDATED_AT", columnDefinition = "TIMESTAMP")
    private OffsetDateTime updatedAt;
}
