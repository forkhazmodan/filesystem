package com.filesystem.repositories;

import com.filesystem.models.Folder;
import com.filesystem.utils.DB;
import com.filesystem.utils.Path;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class FolderRepository {

    public static boolean isFolderExist(String fullPathName) {
        try{
            getFolder(fullPathName);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static boolean isFolderExist(String pathName, Folder parent) {
        try{
            getFolder(pathName, parent);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static Folder findFolder(String pathName, Folder parent) {
        try{
            return getFolder(pathName, parent);
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Folder findFolder(String fullPathName) {
        try{
            return getFolder(fullPathName);
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Folder getFolder(String pathName, Folder parent) throws NoResultException {
        TypedQuery<Folder> accountQuery = DB.em().createNamedQuery("findFolder", Folder.class);
        accountQuery.setParameter("name", pathName);
        accountQuery.setParameter("parent", parent);
        return accountQuery.getSingleResult();
    }

    public static Folder getFolder(String fullPathName) throws NoResultException {

        Path path = new Path(fullPathName);
        String[] parts = path.getParts();

        Folder parent = null;
        Folder current = null;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            current = getFolder(part, parent);

            parent = current;
        }

        return current;
    }

}
