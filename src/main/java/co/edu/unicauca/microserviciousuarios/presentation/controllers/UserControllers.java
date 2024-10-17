package co.edu.unicauca.microserviciousuarios.presentation.controllers;

import co.edu.unicauca.microserviciousuarios.aplication.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.unicauca.microserviciousuarios.presentation.dto.UserDTO;


@RestController
@RequestMapping("/api/users")
public class UserControllers {
    @Autowired
    private UserServices userServices;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        if(userDTO.getName().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("name cant be empty");
        if(userDTO.getPassword() == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("password is needed");
        if(userDTO.getPassword().isEmpty() || userDTO.getPassword().length() < 8)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("password is needed y and more than 7 characters");
        if(userDTO.getEmail().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The email is needed");
        if(userDTO.getAddress().isEmpty())
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("The address is needed");
        if(userDTO.getPhone() <100)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Phone number is needed");
        if(userDTO.getRol().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Rol is required");

        if(userDTO.getRol().compareTo("organizer") != 0 && userDTO.getRol().compareTo("author") != 0 )
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("rol must be organizer or author");

        UserDTO createdUser = userServices.createUser(userDTO);
        if(createdUser == null)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sorry, some issue creating user");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<Object> getUser(@PathVariable String idUser) {
        try{
            UserDTO users = userServices.findUserById(idUser);

            if(users == null)
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("user with id " + idUser + " not found");
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<Object> updateUser(@PathVariable String idUser, @RequestBody UserDTO userDTO) {
        UserDTO userUpdated = userServices.updateUser(idUser, userDTO);
        if(userUpdated == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("sorry, we cant update the user");
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Object> deleteUser(@PathVariable String idUser) {
        UserDTO userDTO = userServices.deleteUser(idUser);
        if( userDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
