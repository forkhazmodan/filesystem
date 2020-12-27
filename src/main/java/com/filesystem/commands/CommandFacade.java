package com.filesystem.commands;

import com.filesystem.annotations.Command;
import com.filesystem.exceptions.CommandErrorException;
import org.reflections8.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandFacade {

    public static void run(String command) throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {

        String packageName = new CommandFacade().getClass().getPackage().getName();
        Reflections reflections = new Reflections(packageName);

        try {

            CommandInterface newCommand = null;
            String regex = null;

            /** Try found command class marked @Command annotation by regex*/
            for (Class<?> commandClass : reflections.getTypesAnnotatedWith(Command.class)) {
                regex = commandClass.getAnnotation(Command.class).regex();
                command = command.trim();

                if (Pattern.matches(regex, command)) {
                    newCommand = (CommandInterface) commandClass
                            .getDeclaredConstructor()
                            .newInstance();
                    break;
                }
            }

            if (newCommand == null) {
                throw new CommandErrorException("Command not found");
            }

            /** Check if any named params matches to params in command class*/
            for (Field field : newCommand.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(command);

                if (matcher.find()) {
                    String value = matcher.group(fieldName);
                    field.setAccessible(true);
                    field.set(newCommand, value);
                }
            }

            newCommand.run();

        } catch (CommandErrorException e) {
            System.out.println(e.getMessage());
        }


    }

    public static void commandList() {
        System.out.println("-----------List of commands-----------");
        String packageName = new CommandFacade().getClass().getPackage().getName();
        Reflections reflections = new Reflections(packageName);

        for (Class<?> commandClass : reflections.getTypesAnnotatedWith(Command.class)) {
            String description = commandClass.getAnnotation(Command.class).description();
            System.out.println(description);
        }

        System.out.println("======================================");
    }
}
