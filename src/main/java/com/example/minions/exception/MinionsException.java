package com.example.minions.exception;

import com.example.minions.model.CustomError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinionsException extends RuntimeException{
    HttpStatus status;
    CustomError error;

    public static MinionsException notFound(String message) {
        return MinionsException.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(CustomError.builder()
                        .code("404")
                        .message(message)
                        .build())
                .build();
    }

    public static MinionsException badRequest(String message) {
        return MinionsException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(CustomError.builder()
                        .code("400")
                        .message(message)
                        .build())
                .build();
    }

    public static MinionsException badGateway(String message) {
        return MinionsException.builder()
                .status(HttpStatus.BAD_GATEWAY)
                .error(CustomError.builder()
                        .code("502")
                        .message(message)
                        .build())
                .build();
    }

    public static MinionsException internalServerError(String message) {
        return MinionsException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(CustomError.builder()
                        .code("500")
                        .message(message)
                        .build())
                .build();
    }
    public static MinionsException accessDenied (String message) {
        return MinionsException.builder()
                .status(HttpStatus.FORBIDDEN)
                .error(CustomError.builder()
                        .code("403")
                        .message(message)
                        .build())
                .build();
    }
}
