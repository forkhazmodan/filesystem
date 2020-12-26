package com.filesystem.exceptions;

public class CommandErrorException extends RuntimeException {
    public CommandErrorException(String message) {
        super(message);
    }
}
