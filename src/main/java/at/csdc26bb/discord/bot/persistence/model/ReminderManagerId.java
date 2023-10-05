package at.csdc26bb.discord.bot.persistence.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReminderManagerId implements Serializable {
    private Long guildId;
    private String role;
}
