package co.edu.unicauca.microserviciousuarios.presentation.controllers;

import co.edu.unicauca.microserviciousuarios.aplication.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unicauca.microserviciousuarios.presentation.dto.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UserControllers {
    @Autowired
    private UserServices userServices;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userServices.createUser(userDTO);
    }
}
