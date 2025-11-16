package me.jdcomputers.commands;

import me.jdcomputers.commands.spleef.SpleefCommand;

public final class CommandCollection {
    public static final Command SPLEEF_COMMAND = new SpleefCommand();
    public static final Command[] COMMANDS = new Command[] {
            SPLEEF_COMMAND
    };
}
