package co.edu.unicauca.microserviciousuarios.domain.model.exceptions;

import lombok.Getter;

@Getter
public class InvalidUserInformation extends Exception{

    private final String invalidData;
    public InvalidUserInformation(String invalidData){
        this.invalidData = invalidData;
    }

}
