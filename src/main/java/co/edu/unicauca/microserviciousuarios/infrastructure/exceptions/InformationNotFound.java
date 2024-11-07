package co.edu.unicauca.microserviciousuarios.infrastructure.exceptions;

public class InformationNotFound extends RuntimeException {
    public InformationNotFound(String message) {
        super(message);
    }
}
