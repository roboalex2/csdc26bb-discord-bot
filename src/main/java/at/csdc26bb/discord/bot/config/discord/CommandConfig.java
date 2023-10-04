package at.csdc26bb.discord.bot.config.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class CommandConfig {

    private final JDA jdaInstance;
    private final List<ListenerAdapter> eventListeners;
    @EventListener(ApplicationReadyEvent.class)
    public void configureRuntime() {
        jdaInstance.addEventListener(eventListeners.toArray());

        jdaInstance.updateCommands().addCommands(
                Commands.slash("notify", "Get updates regarding BurningSeries episodes")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.EMPTY_PERMISSIONS))
                        .addSubcommands(
                                new SubcommandData("add", "Add a new series to be notified about.")
                                        .addOptions(
                                                new OptionData(OptionType.STRING, "url", "The URL to the series (alternatively: the EXACT name of the series as shown on 'list')")
                                                        .setRequired(true).setAutoComplete(true)
                                        ),
                                new SubcommandData("remove", "Remove a series from your notification list.")
                                        .addOptions(
                                                new OptionData(OptionType.STRING, "url", "The URL to the series (alternatively: the EXACT name of the series as shown on 'list')")
                                                        .setRequired(true).setAutoComplete(true)
                                        ),
                                new SubcommandData("list", "List all series you are notified about.")
                        )
        ).queue();
    }
}
