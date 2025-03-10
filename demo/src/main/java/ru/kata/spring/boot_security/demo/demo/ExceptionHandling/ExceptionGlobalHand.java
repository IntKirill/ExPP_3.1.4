package ru.kata.spring.boot_security.demo.demo.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHand {

    @ExceptionHandler
    public ResponseEntity<UserIncorentData> handlerExeption
            (NoSuchUserException e) {
        UserIncorentData data = new UserIncorentData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorentData> handlerExeption
            (Exception e) {
        UserIncorentData data = new UserIncorentData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
