package test;

import aplication.UserServices;
import domain.model.User;
import infrastructure.repositories.mongoDB.UserRepositoyMongo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class testUserServices {

    @InjectMocks
    private UserServices userService;

    @Mock
    private UserRepositoyMongo userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }


    @Test
    void testCreateUser() {
        User newUser= new User("1", "Juan" ,"Juan@gmail.com", "12345678", "Cra452", 1111111111);
        Assertions.assertNotNull(userRepository.createUser(newUser));

    }
}
