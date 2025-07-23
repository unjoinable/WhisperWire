import org.jspecify.annotations.NullMarked;

@NullMarked
module WhisperWire.main {
    requires org.tomlj;
    requires org.jspecify;
    requires net.dv8tion.jda;
    requires org.slf4j;
    requires net.minestom.server;
    requires org.jetbrains.annotations;
    requires net.kyori.adventure;
    requires net.kyori.adventure.nbt;
    requires net.kyori.adventure.key;
    requires net.kyori.examination.api;
}