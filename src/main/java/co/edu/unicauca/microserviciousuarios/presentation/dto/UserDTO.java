package co.edu.unicauca.microserviciousuarios.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String rol;
    private int phone;

    public UserDTO() {}

}
