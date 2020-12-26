package com.filesystem.commands;

import com.filesystem.annotations.Command;
import com.filesystem.services.FileSystemService;

import java.io.File;

@Command(
        regex = "^\\/ls (?<path>(.+\\/?)([^\\/\\s]+)?)",
        description = "/ls <path> - folder content"
)
public class LsCommand implements CommandInterface {

    String path;

    @Override
    public void run() {
        FileSystemService.folderList(new File(path));
    }
}
