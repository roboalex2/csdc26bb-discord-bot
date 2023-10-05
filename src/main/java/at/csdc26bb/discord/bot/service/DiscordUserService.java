package at.csdc26bb.discord.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordUserService {

    private final JDA jdaInstance;

    public User getUser(long userId) {
        User user = jdaInstance.getUserById(userId);
        if (user == null) {
            user = jdaInstance.retrieveUserById(userId).complete();
        }
        return user;
    }

    public List<User> getUsersByRole(long guildId, String roleName) {
        Guild guild = jdaInstance.getGuildById(guildId);
        if (guild == null) {
            return List.of();
        }

        return guild.findMembers(member -> member.getRoles()
                        .stream()
                        .map(Role::getName)
                        .anyMatch(roleName::equals)
                ).get().stream()
                .map(Member::getUser)
                .toList();
    }
}
