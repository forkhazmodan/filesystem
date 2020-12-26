package com.filesystem.commands;

import com.filesystem.annotations.Command;
import com.filesystem.services.FileSystemService;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
import java.io.File;
import java.sql.SQLIntegrityConstraintViolationException;

@Command(
        regex = "^\\/upload (?<path>.+(\\/|\\\\))(?<filename>.*)$",
        description = "/upload <path/to/file.ext>"
)
public class UploadCommand implements CommandInterface {

    String path;

    String filename;

    @Override
    public void run() {

        try {
            String filePathString = path + filename;

            File f = new File(filePathString);

            if(!f.exists()) System.out.println("File do not exists");

            if(f.isFile()) {
                FileSystemService.uploadFile(f);
            } else if(f.isDirectory()) {
                FileSystemService.uploadFolder(f);
            }

        } catch (PersistenceException exception) {
            //TODO refactor this block later
            if(exception.getCause() instanceof ConstraintViolationException) {
                if(exception.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                    System.out.println(exception.getCause().getCause().getMessage());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
