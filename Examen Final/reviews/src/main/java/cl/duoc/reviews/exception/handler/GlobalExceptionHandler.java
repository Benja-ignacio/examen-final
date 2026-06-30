package cl.duoc.reviews.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.reviews.exception.custom.ResourceNotFoundException;
import cl.duoc.reviews.exception.custom.UnauthorizedException;

@RestController
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

            
    @ExceptionHandler(Exception.class) // EXCEPCIONES GLOBALES 
    public ResponseEntity<Map<String, String>> handleAll(Exception ex) {

        // log interno completo
        logger.error("Error inesperado", ex);

        // respuesta limpia al cliente
        Map<String, String> response = Map.of(
                "error", "Error interno del servidor"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // ERRORES DE VALID
    public ResponseEntity<Map<String, String>> validExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        logger.warn("Error de validación: {}", ex.getMessage());
        
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> notificationNotFound(ResourceNotFoundException ex) {

        logger.warn("Resource Not Found: {}", ex.getMessage()); // LOGS QUE SOLO SE PUEDE VER INTERNAMENTE

        Map<String, String> error = Map.of(
            "error", ex.getMessage() // ERROR QUE VE EL FRONTEND
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> notificationNotFound(UnauthorizedException ex) {

        logger.warn("Error: Not Authorized: {}", ex.getMessage()); // LOGS QUE SOLO SE PUEDE VER INTERNAMENTE

        Map<String, String> error = Map.of(
            "error", ex.getMessage() // ERROR QUE VE EL FRONTEND
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
