package co.edu.unicauca.microserviciousuarios.aplication;


import co.edu.unicauca.microserviciousuarios.domain.model.IUserRepository;
import co.edu.unicauca.microserviciousuarios.domain.model.User;
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
    private co.edu.unicauca.microserviciousuarios.presentation.mapper.mapper mapper;

    @Autowired
    public UserServices(IUserRepository repository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    /**
     *
     * @param idUserToFind
     * @return UserDTO, if is succesfully return UserDTO find, else, return null
     *
     */
    public UserDTO findUserById(String idUserToFind){
        User userEntity = this.repository.findUserById(idUserToFind);
        if(userEntity != null){
                UserDTO userWithoutPassword = modelMapper.map(userEntity, UserDTO.class);
                userWithoutPassword.setPassword(null);
                return userWithoutPassword;

        }
        return null;
    }

    /**
     *
     * @param userDTO
     * @return in the case of succesfuly, the function return the UserDTO to save, else, the function return null
     *
     * Here, the function validates that the password length is 8 characters or more for cerate a encript password and save the entitity whit
     * the encrypt password
     */
    public UserDTO createUser (UserDTO userDTO) {
        User userEntity = this.modelMapper.map(userDTO, User.class);
        String oldPassword = userEntity.getPassword();
        if(oldPassword.length() < 8){
            return null;
        }
        String fortePasword  =  convertirSHA256(oldPassword);
        userEntity.setPassword(fortePasword);
        User userSave =this.repository.createUser(userEntity);
        if( userSave != null){
            //Notificar al broker
            return this.modelMapper.map(userSave, UserDTO.class);
        }else {
            return null;
        }
    }

    /**
     *
     * @param idUserToUpdate id to the old User
     * @param userDTO new information of user
     * @return in the case of succesfuly, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO updateUser(String idUserToUpdate, UserDTO userDTO) {

        User userEntity = modelMapper.map(userDTO, User.class);
        User userUpdated = this.repository.updateUserById(idUserToUpdate, userEntity);
        if( userUpdated == null)
            return null;
        return this.modelMapper.map(userUpdated, UserDTO.class);
    }

    /**
     *
     * @param idUserToDelete
     * @returnin the case of succesfuly, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO deleteUser(String idUserToDelete) {
        User userEntity = this.repository.findUserById(idUserToDelete);
        if(this.repository.deleteUserById(idUserToDelete) != null){
            return this.modelMapper.map(userEntity, UserDTO.class);
        }else{
            return null;
        }
    }

    /**
     *
     * @param email
     * @param password
     * @return in the case of succesfuly, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO loginUser(String email, String password) {
        return this.modelMapper.map(this.repository.loginUser(email, password), UserDTO.class);
    }


    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();

        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
