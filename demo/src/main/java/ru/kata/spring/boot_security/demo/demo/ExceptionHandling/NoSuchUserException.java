package ru.kata.spring.boot_security.demo.demo.ExceptionHandling;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String message) {
        super(message);
    }
}
