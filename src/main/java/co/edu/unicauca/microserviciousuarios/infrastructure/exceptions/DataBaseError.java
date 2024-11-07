package co.edu.unicauca.microserviciousuarios.infrastructure.exceptions;

public class DataBaseError extends RuntimeException {
    public DataBaseError(String message) {
        super(message);
    }
}
