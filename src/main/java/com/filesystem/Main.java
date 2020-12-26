package com.filesystem;

import com.filesystem.commands.CommandFacade;
import com.filesystem.utils.DB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {

        emf = Persistence.createEntityManagerFactory("PersistenceJPA");
        em = emf.createEntityManager();
        new DB(em);

        Scanner sc = new Scanner(System.in);

        try {
            CommandFacade.commandList();

            while (true) {
                String line = sc.nextLine().trim();

                if(line.length() > 0) {
                    CommandFacade.run(line);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            sc.close();
            em.close();
            emf.close();
        }
    }
}
