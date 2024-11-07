package co.edu.unicauca.microserviciousuarios.domain.model;

import co.edu.unicauca.microserviciousuarios.domain.model.exceptions.InvalidUserInformation;

public interface IUserRepository {
    User createUser(User userToCreate) throws InvalidUserInformation;
    User findUserById(String id) throws InvalidUserInformation;
    User updateUserById(String id, User userToUpdate) throws InvalidUserInformation;
    User deleteUserById(String id) throws InvalidUserInformation;
    User loginUser(String email, String password ) throws InvalidUserInformation;

}
