package com.filesystem.commands;

import com.filesystem.annotations.Command;
import com.filesystem.services.FileSystemService;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
import java.io.File;
import java.sql.SQLIntegrityConstraintViolationException;

@Command(
        regex = "^\\/create:file (?<path>.+(\\/|\\\\))(?<filename>.*)$",
        description = "/create:file <path/to/file.ext> - create file"
)
public class CreateFileCommand implements CommandInterface {

    public String path;

    public String filename;

    @Override
    public void run() {
        try {
            File f = new File(path + filename);
            FileSystemService.uploadFile(f);
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
