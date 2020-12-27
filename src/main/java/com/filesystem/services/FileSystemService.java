package com.filesystem.services;

import com.filesystem.models.File;
import com.filesystem.models.Folder;
import com.filesystem.repositories.FileRepository;
import com.filesystem.repositories.FolderRepository;
import com.filesystem.utils.DB;
import com.filesystem.utils.Path;

import javax.persistence.NoResultException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSystemService {

    public static Folder createFolder(String pathString) throws Exception {

        Path path = new Path(pathString);

        Folder previousFolder = null;

        DB.em().getTransaction().begin();
        try {

            for (String part : path.getParts()) {
                if (!"".equals(part)) {

                    Folder folder = null;

                    try {
                        folder = FolderRepository.getFolder(part, previousFolder);
                    } catch (NoResultException e) {
                    }

                    if (folder == null) {
                        folder = new Folder(part);
                        if (previousFolder != null) {
                            previousFolder.addChildren(folder);
                        }
                    }

                    previousFolder = folder;

                    DB.em().persist(folder);
                }
            }

            DB.em().getTransaction().commit();

        } catch (Exception ex) {
            DB.em().getTransaction().rollback();
            throw ex;
        }

        return previousFolder;
    }

    public static Folder createFolder(java.io.File file) throws Exception {
        return FileSystemService.createFolder(file.getAbsolutePath());
    }

    public static void uploadFile(java.io.File fileToUpload) throws Exception {

        Folder folder = null;
        folder = createFolder(fileToUpload.getParent());

        DB.em().getTransaction().begin();
        try {

            byte[] byteArray;
            if (fileToUpload.length() > 0) {
                //First read file into array of bytes
                byteArray = new byte[(int) fileToUpload.length()];
                FileInputStream fileInputStream = new FileInputStream(fileToUpload);
                fileInputStream.read(byteArray);
                fileInputStream.close();
            } else {
                //Create empty file with empty arrayOfBytes
                byteArray = new byte[0];
                FileOutputStream fileOutputStream = new FileOutputStream(fileToUpload);
                fileOutputStream.write(byteArray);
                fileOutputStream.close();
            }

            System.out.println(fileToUpload);

            File file = new File();
            file.setFolder(folder);
            file.setName(fileToUpload.getName());
            file.setData(byteArray);

            folder.addFile(file);

            DB.em().persist(file);
            DB.em().getTransaction().commit();

        } catch (Exception ex) {
            DB.em().getTransaction().rollback();
            throw ex;
        }
    }

    public static void uploadFolder(java.io.File folderToUpload) {
        try {

            java.io.File f = folderToUpload;

            if (f.isDirectory()) {

                for (java.io.File file : f.listFiles()) {

                    if (file.isFile()) {
                        try {
                            FileSystemService.uploadFile(file);
                        } catch (Exception e) {

                            String msg = "file: %s was nt created cause: %s";
                            System.out.println(String.format(msg, file.getAbsolutePath(), e.getCause().getClass().toString()));
                        }
                    }

                    if (file.isDirectory()) {
                        FileSystemService.createFolder(file);
                        FileSystemService.uploadFolder(file);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void folderList(java.io.File fileToList) {
        try {
            Folder rootFolder = FolderRepository.getFolder(fileToList.getAbsolutePath());

            System.out.println(fileToList.getAbsolutePath() + ">");

            for (Folder folder : rootFolder.getChildren()) {
                System.out.println(folder.getName() + "\\");
            }

            for (File file : rootFolder.getFiles()) {
                System.out.println(file.getName());
            }
        } catch (NoResultException e) {
            System.out.println("Folder do not exists");
        }
    }

    public static void download(String from, String to) {

        java.io.File fromFile = new java.io.File(from);
        String fromAbsolutePath = fromFile.getAbsolutePath();
        String fromParentPath = fromFile.getParent();
        String fromFileName = fromFile.getName();

        try {

            Folder childFolder = FolderRepository.findFolder(fromAbsolutePath);
            Folder parentFolder = FolderRepository.findFolder(fromParentPath);
            File parentFolderFile = FileRepository.findFile(parentFolder, fromFileName);
            java.io.File targetFolder = new java.io.File(to);

            if (childFolder != null) {
                downloadFolder(childFolder, targetFolder);
            } else if (parentFolderFile != null) {
                downloadFile(parentFolderFile, targetFolder);
            } else {
                throw new NoResultException("Not found");
            }

        } catch (NoResultException e) {
            System.out.println(from + " Not found");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void downloadFile(File fileToDownload, java.io.File targetFile) throws IOException {
        if (fileToDownload == null) {
            System.out.println("No such file");
            return;
        }

        java.io.File parentDir = new java.io.File(targetFile.getParent());
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (targetFile.exists()) {
            targetFile = createCopyFile(targetFile);
        } else {
            targetFile.createNewFile();
        }

        byte[] byteArray = fileToDownload.getData();
        FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(byteArray);
        fos.close();

        System.out.println(targetFile.getAbsolutePath() + " created!");
    }

    public static void downloadFolder(Folder folderToDownload, java.io.File targetFolder) throws IOException {

        if (folderToDownload == null) {
            System.out.println(folderToDownload.getName());
            return;
        }

        targetFolder.mkdirs();

        for (File fileItem : folderToDownload.getFiles()) {
            String pathName = targetFolder.getAbsolutePath() + java.io.File.separator + fileItem.getName();
            FileSystemService.downloadFile(fileItem, new java.io.File(pathName));
        }

        for (Folder folderItem : folderToDownload.getChildren()) {
            String pathName = targetFolder.getAbsolutePath() + java.io.File.separator + folderItem.getName();
            downloadFolder(folderItem, new java.io.File(pathName));
        }
    }

    private static java.io.File createCopyFile(java.io.File targetFile) {

        String name = targetFile.getName();
        String fName = name.substring(0, name.lastIndexOf("."));
        String fExt = name.substring(name.lastIndexOf("."));

        targetFile = new java.io.File(targetFile.getParent() + "/" + fName + " - copy" + fExt);

        if (targetFile.exists()) {
            targetFile = createCopyFile(targetFile);
        }

        return targetFile;
    }
}
