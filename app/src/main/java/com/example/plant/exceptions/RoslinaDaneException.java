package com.example.plant.exceptions;

//Wywołanie wyjątku z niestandardowym tekstem dzięki czemu użytkownik otrzymuje powiadomienie.
public class RoslinaDaneException extends RuntimeException{

    public RoslinaDaneException(String message) {
        super(message);
    }
}
