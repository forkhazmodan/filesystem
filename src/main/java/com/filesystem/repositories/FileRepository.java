package com.filesystem.repositories;

import com.filesystem.models.File;
import com.filesystem.models.Folder;
import com.filesystem.utils.DB;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class FileRepository {

    public static File findFile(Folder folder, String fileName){
        try{
            return FileRepository.getFile(folder, fileName);
        } catch (NoResultException e) {
            return null;
        }
    }

    public static File getFile(Folder folder, String fileName) throws NoResultException {
        TypedQuery<File> accountQuery = DB.em().createNamedQuery("findFile", File.class);
        accountQuery.setParameter("name", fileName);
        accountQuery.setParameter("folder", folder);
        return accountQuery.getSingleResult();
    }
}
