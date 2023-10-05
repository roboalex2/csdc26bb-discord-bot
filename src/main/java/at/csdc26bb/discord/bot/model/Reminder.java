package at.csdc26bb.discord.bot.model;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reminder {
    @Nullable
    @EqualsAndHashCode.Include
    private Long id; // Nullable because of that Long
    private long guildId;
    @EqualsAndHashCode.Include
    private String receiverRole;
    @EqualsAndHashCode.Include
    private OffsetDateTime remindAt;
    @EqualsAndHashCode.Include
    private String titel;
    @Nullable
    private String description;
    @Nullable
    private Long createdBy;
    private Boolean active = true;
}
