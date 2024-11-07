package co.edu.unicauca.microserviciousuarios.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class DuplicateInformation extends RuntimeException {
    private final String duplicateFileds;
    public DuplicateInformation(String message, String duplicateFileds) {

        super(message);
        this.duplicateFileds = duplicateFileds;
    }

}
