package co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.repositories;

import co.edu.unicauca.microserviciousuarios.domain.model.IUserRepository;
import co.edu.unicauca.microserviciousuarios.domain.model.User;
import co.edu.unicauca.microserviciousuarios.domain.model.exceptions.InvalidUserInformation;
import co.edu.unicauca.microserviciousuarios.infrastructure.exceptions.DataBaseError;
import co.edu.unicauca.microserviciousuarios.infrastructure.exceptions.DuplicateInformation;
import co.edu.unicauca.microserviciousuarios.infrastructure.exceptions.InformationNotFound;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.documents.UserDocument;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.mappers.UserMapper;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.mongoRepositories.MongoRespositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryMongo implements IUserRepository {

    private final MongoRespositoryUser mongoRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    public UserRepositoryMongo(MongoRespositoryUser mongoRepository, MongoTemplate mongoTemplate){
        this.mongoRepository = mongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * @param userToCreate add new user in the database
     * @return userCreated
     */
    @Override
    public User createUser(User userToCreate) throws InvalidUserInformation {
        userToCreate.setId(null);
        UserDocument userDocument = UserMapper.toUserDocument(userToCreate);
        UserDocument createdUser = null;
        try{
            createdUser = mongoRepository.save(userDocument);
        }catch (DuplicateKeyException e){
            throw new DuplicateInformation(e.getMessage(), "Email allready exist");
        }catch (Exception e){
            throw new DataBaseError(e.getMessage());
        }
        return UserMapper.toUser(createdUser);
    }
    @Override
    public User findUserById(String id) throws InvalidUserInformation {
        UserDocument userFounded = mongoRepository.findById(id).orElse(null);
        if(userFounded==null || !userFounded.isActive())
            throw new InformationNotFound("The user doesnt exist");
        return UserMapper.toUser(userFounded);
    }
    @Override
    public User updateUserById(String id, User newUser) throws InvalidUserInformation {
        try{
            Optional<UserDocument> userOptional = mongoRepository.findById(id);

            if(userOptional.isEmpty())
                throw new InformationNotFound("User not found");

            UserDocument userDocument = userOptional.get();

            if(newUser.getName() !=null && !newUser.getName().isEmpty())
                userDocument.setName(newUser.getName());
            if(newUser.getEmail() !=null && !newUser.getEmail().isEmpty())
                userDocument.setEmail(newUser.getEmail());
            if(newUser.getPassword() !=null && !newUser.getPassword().isEmpty())
                userDocument.setPassword(newUser.getPassword());
            if( newUser.getAddress() !=null && !newUser.getAddress().isEmpty())
                userDocument.setAddress(newUser.getAddress());
            if(newUser.getPhone() != 0)
                userDocument.setPhone(newUser.getPhone());

            UserDocument updatedUser = null;

            updatedUser = mongoRepository.save(userDocument);

            return UserMapper.toUser(updatedUser);
        }catch (DuplicateKeyException e){
            throw new DuplicateInformation(e.getMessage(), "Email allready exist");
        }catch (Exception e){
            throw new DataBaseError(e.getMessage());
        }
    }
    @Override
    public User deleteUserById(String id) throws InvalidUserInformation {
        try {
            Optional<UserDocument> userOptional = mongoRepository.findById(id);

            if(userOptional.isEmpty())
                throw new InformationNotFound("User not found");

            UserDocument userDocument = userOptional.get();
            userDocument.setActive(false);

            UserDocument userDeleted =  mongoRepository.save(userDocument);

            return UserMapper.toUser(userDeleted);
        }catch (Exception e){
            throw new DataBaseError(e.getMessage());
        }
    }
    @Override
    public User loginUser(String email, String password ) throws InvalidUserInformation {
        try {
            Query query = new Query();

            query.addCriteria(Criteria.where("email").is(email).and("password").is(password));

            UserDocument userDocument = mongoTemplate.findOne(query, UserDocument.class);

            if (userDocument == null)
                throw new InformationNotFound("Invalid email and password");
            return UserMapper.toUser(userDocument);
        } catch (Exception e) {
            throw new DataBaseError(e.getMessage());
        }
    }
}
