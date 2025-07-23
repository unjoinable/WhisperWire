package io.github.unjoinable.whisperwire;

import io.github.unjoinable.whisperwire.config.RuntimeContext;
import io.github.unjoinable.whisperwire.core.bridge.AbstractBridge;
import io.github.unjoinable.whisperwire.core.endpoint.impls.DiscordEndpoint;
import io.github.unjoinable.whisperwire.core.endpoint.impls.MinestomEndpoint;
import io.github.unjoinable.whisperwire.listener.GlobalChatListener;
import io.github.unjoinable.whisperwire.listener.GlobalMessageListener;
import io.github.unjoinable.whisperwire.utils.DiscordUtils;
import net.dv8tion.jda.api.JDA;

import java.util.Objects;

import static net.minestom.server.MinecraftServer.getConnectionManager;
import static net.minestom.server.MinecraftServer.getGlobalEventHandler;

/**
 * {@code GlobalBridge} establishes a two-way message bridge between
 * a Minestom server and a Discord channel.
 * <p>
 * It wires together:
 * <ul>
 *     <li>{@link MinestomEndpoint} for Minecraft-side communication</li>
 *     <li>{@link DiscordEndpoint} for Discord-side communication</li>
 *     <li>Message listeners to synchronize chat in both directions</li>
 * </ul>
 * Listeners are automatically registered during construction
 * and unregistered during {@link #stop()}.
 */
public class GlobalBridge extends AbstractBridge {

    private final JDA jda;

    private final GlobalChatListener minestomListener;
    private final GlobalMessageListener discordListener;

    /**
     * Constructs a new {@code GlobalBridge}, sets up endpoints and registers listeners
     * for global chat bridging between Minestom and Discord.
     *
     * @param jda the active {@link JDA} instance
     * @param ctx the runtime configuration context
     */
    public GlobalBridge(JDA jda, RuntimeContext ctx) {
        this.jda = jda;

        var minestomEndpoint = new MinestomEndpoint(() -> getConnectionManager().getOnlinePlayers());
        var discordEndpoint = new DiscordEndpoint(
                Objects.requireNonNull(
                        DiscordUtils.textChannelById(jda, ctx.discordConfig().chatChannelId()),
                        "Discord channel with ID %s not found".formatted(ctx.discordConfig().chatChannelId())
                )
        );

        this.discordListener = new GlobalMessageListener(this, discordEndpoint, ctx.discordConfig());
        this.minestomListener = new GlobalChatListener(this, minestomEndpoint);

        jda.addEventListener(discordListener);
        getGlobalEventHandler().addListener(minestomListener);
    }

    /**
     * Unregisters all previously registered listeners from Discord and Minestom.
     * Call this when shutting down or reinitializing the bridge.
     */
    @Override
    public void stop() {
        super.stop();
        jda.removeEventListener(discordListener);
        getGlobalEventHandler().removeListener(minestomListener);
    }
}