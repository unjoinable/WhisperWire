package io.github.unjoinable.whisperwire;

import io.github.unjoinable.whisperwire.config.ConfigReader;
import io.github.unjoinable.whisperwire.config.RuntimeContext;

public class WhisperWire {
    public static void main(String[] args) {
        RuntimeContext ctx = new ConfigReader().read();
        new DiscordBot(ctx.discordConfig()).start();
    }
}
