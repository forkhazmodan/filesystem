package com.filesystem.commands;

import com.filesystem.annotations.Command;

@Command(
        regex = "^\\/help",
        description = "/help - list of commands"
)
public class HelpCommand implements CommandInterface {

    @Override
    public void run() {
        CommandFacade.commandList();
    }
}
