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
    public UserServices(IUserRepository repository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    /**
     *
     * @param idUserToFind id user To find
     * @return UserDTO, if is successfully return UserDTO find, else, return null
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
     * Here, the function validates that the password length is 8 characters or more for create an encrypt password and save the entity with
     * the encrypt password
     * @param userDTO info of user to create
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO createUser (UserDTO userDTO) {
        //validate the name is not empty
        if(userDTO.getName() == null || userDTO.getName().isEmpty()){
            return null;
        }
        if(userDTO.getPhone() < 0 ){
            return null;
        }
    

        User userEntity = this.modelMapper.map(userDTO, User.class);
        String oldPassword = userEntity.getPassword();
        if(oldPassword.length() < 8){
            return null;
        }
        String fortePassword  =  stringToSHA256(oldPassword);
        userEntity.setPassword(fortePassword);
        User userSave =this.repository.createUser(userEntity);
        if( userSave != null){
            //TODO notify the broker
            return this.modelMapper.map(userSave, UserDTO.class);
        }else {
            return null;
        }
    }

    /**
     *
     * @param idUserToUpdate id to the old User
     * @param userDTO new information of user
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO updateUser(String idUserToUpdate, UserDTO userDTO) {
        //validate the name is not empty
        if(userDTO.getName() == null || userDTO.getName().isEmpty()){
            return null;
        }
        if(userDTO.getPhone() < 0 ){
            return null;
        }

        User userEntity = modelMapper.map(userDTO, User.class);
        User userUpdated = this.repository.updateUserById(idUserToUpdate, userEntity);
        if( userUpdated == null)
            return null;
        return this.modelMapper.map(userUpdated, UserDTO.class);
    }

    /**
     *
     * @param idUserToDelete id of user to delete
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
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
     * @param email email to find
     * @param password password without encrypt to find
     * @return in the case of successfully, the function return the UserDTO to save, else, the function return null
     */
    public UserDTO loginUser(String email, String password) {
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
