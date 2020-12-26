package com.filesystem.commands;

import com.filesystem.annotations.Command;
import com.filesystem.services.FileSystemService;


@Command(
        regex = "^\\/create:folder (?<path>(.+\\/?)([^\\/\\s]+)?)",
        description = "/create:folder <path> - create folder"
)
public class CreateFolderCommand implements CommandInterface {

    public String path;

    @Override
    public void run() {

        try {
            FileSystemService.createFolder(path);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
