package co.edu.unicauca.microserviciousuarios;

import co.edu.unicauca.microserviciousuarios.aplication.UserServices;
import co.edu.unicauca.microserviciousuarios.domain.model.IUserRepository;
import co.edu.unicauca.microserviciousuarios.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import co.edu.unicauca.microserviciousuarios.presentation.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class testUserServices {

    @Mock
    private IUserRepository userRepository; // Simulamos el repositorio

    @Mock
    private ModelMapper modelMapper; // Simulamos el ModelMapper

    @InjectMocks
    private UserServices userServices; // Inyectamos el mock del repositorio y ModelMapper en el servicio


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

}
