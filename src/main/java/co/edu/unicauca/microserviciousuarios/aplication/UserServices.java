package co.edu.unicauca.microserviciousuarios.aplication;


import co.edu.unicauca.microserviciousuarios.domain.model.IUserRepository;
import co.edu.unicauca.microserviciousuarios.domain.model.User;
import co.edu.unicauca.microserviciousuarios.domain.model.exceptions.InvalidUserInformation;
import co.edu.unicauca.microserviciousuarios.infrastructure.broker.rabbit.MessageProducer;
import co.edu.unicauca.microserviciousuarios.infrastructure.exceptions.DataBaseError;
import co.edu.unicauca.microserviciousuarios.infrastructure.exceptions.DuplicateInformation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unicauca.microserviciousuarios.presentation.dto.UserDTO;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class UserServices{
    @Autowired
    private IUserRepository repository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageProducer producer;

    @Autowired
    public UserServices(IUserRepository repository, ModelMapper modelMapper, MessageProducer producer) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.producer = producer;
    }

    /**
     *
     * @param idUserToFind id user To find
     * @return UserDTO, if is successfully return UserDTO find, else, return null
     *
     */
    public UserDTO findUserById(String idUserToFind) throws InvalidUserInformation {
        User userEntity = this.repository.findUserById(idUserToFind);
        UserDTO userWithoutPassword = modelMapper.map(userEntity, UserDTO.class);
        userWithoutPassword.setPassword(null);
        return userWithoutPassword;
    }

    /**
     * Here, the function validates that the password length is 8 characters or more for create an encrypt password and save the entity with
     * the encrypt password
     * @param userDTO info of user to create
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO createUser (UserDTO userDTO) throws InvalidUserInformation, DataBaseError, DuplicateInformation {

        User userEntity = this.modelMapper.map(userDTO, User.class);
        String oldPassword = userEntity.getPassword();

        String fortePassword  =  stringToSHA256(oldPassword);
        userEntity.setPassword(fortePassword);

        User userSave =this.repository.createUser(userEntity);

        producer.sendMessage(this.modelMapper.map(userSave, UserDTO.class));

        return this.modelMapper.map(userSave, UserDTO.class);

    }

    /**
     *
     * @param idUserToUpdate id to the old User
     * @param userDTO new information of user
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO updateUser(String idUserToUpdate, UserDTO userDTO) throws InvalidUserInformation {

        User userEntity = modelMapper.map(userDTO, User.class);
        User userUpdated = this.repository.updateUserById(idUserToUpdate, userEntity);
        return this.modelMapper.map(userUpdated, UserDTO.class);
    }

    /**
     *
     * @param idUserToDelete id of user to delete
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO deleteUser(String idUserToDelete) throws InvalidUserInformation {
        User userEntity = this.repository.findUserById(idUserToDelete);
        this.repository.deleteUserById(idUserToDelete);
        return this.modelMapper.map(userEntity, UserDTO.class);
    }

    /**
     *
     * @param email email to find
     * @param password password without encrypt to find
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO loginUser(String email, String password) throws InvalidUserInformation {
        return this.modelMapper.map(this.repository.loginUser(email, password), UserDTO.class);
    }


    public String stringToSHA256(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();

        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
