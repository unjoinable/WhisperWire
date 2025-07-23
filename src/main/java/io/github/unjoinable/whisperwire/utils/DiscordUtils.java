package io.github.unjoinable.whisperwire.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jspecify.annotations.Nullable;

public class DiscordUtils {

    private DiscordUtils() {
        throw new UnsupportedOperationException("Cannot instance a utility class");
    }

    /**
     * Retrieves a {@link TextChannel} from the given channel ID using the provided {@link JDA} instance.
     *
     * @param jda       The JDA instance to query from.
     * @param channelId The ID of the Discord text channel.
     * @return The corresponding {@link TextChannel}, or {@code null} if not found or not a text channel.
     */
    public static @Nullable TextChannel textChannelById(JDA jda, String channelId) {
        return jda.getTextChannelById(channelId);
    }
}
