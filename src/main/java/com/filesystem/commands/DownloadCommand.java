package com.filesystem.commands;

import com.filesystem.annotations.Command;
import com.filesystem.services.FileSystemService;

@Command(
        regex = "^\\/download (?<from>(.+(\\/|\\\\)?)(.*[^\\s]))\\s+(?<to>(.+(\\/|\\\\)?.*[^\\s]))$",
        description = "/download <path/from/file.ext> <path/to/file.ext>"
)
public class DownloadCommand implements CommandInterface {

    String from;

    String to;

    @Override
    public void run() {
        System.out.println(from);
        System.out.println(to);
        FileSystemService.download(from, to);
    }
}