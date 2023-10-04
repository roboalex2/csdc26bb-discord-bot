package at.csdc26bb.discord.bot.config.discord;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "settings.discord")
@Data
@AllArgsConstructor
@Validated
@NoArgsConstructor
public class DiscordConfigProperties {
    @NotEmpty
    private String token;
}
