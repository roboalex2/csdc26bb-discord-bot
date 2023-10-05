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
@Table(name = "reminder")
public class ReminderEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @EqualsAndHashCode.Include
    @Column(name = "receiver_role", length = 64, nullable = false)
    private String receiverRole;

    @EqualsAndHashCode.Include
    @Column(name = "remind_at", columnDefinition = "TIMESTAMP", nullable = false)
    private OffsetDateTime remindAt;

    @EqualsAndHashCode.Include
    @Column(name = "titel", length = 255, nullable = false)
    private String titel;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime updatedAt;
}
