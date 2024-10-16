package domain.model;

public interface IUserRepository {
    User createUser(User userToCreate);
    User findUserById(String id);
    User updateUserById(String id, User userToUpdate);
    User deleteUserById(String id);
    User loginUser(String email, String password );

}
