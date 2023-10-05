package at.csdc26bb.discord.bot.model;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReminderManager {
    private long guildId;
    private String role;
}
