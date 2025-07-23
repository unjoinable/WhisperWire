package io.github.unjoinable.whisperwire;

import io.github.unjoinable.whisperwire.config.ConfigReader;
import io.github.unjoinable.whisperwire.config.RuntimeContext;

public final class WhisperWire {

    public static void main(String[] args) {
        RuntimeContext ctx = new ConfigReader().load();
        DiscordBot bot = new DiscordBot(ctx.discordConfig());
        bot.start();
    }
}