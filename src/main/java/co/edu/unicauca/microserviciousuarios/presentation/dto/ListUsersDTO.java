package co.edu.unicauca.microserviciousuarios.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ListUsersDTO {
    private int totalUsers;
    private List<UserDTO> users;
    public ListUsersDTO() {}
}
