package co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.repositories;

import co.edu.unicauca.microserviciousuarios.domain.model.IUserRepository;
import co.edu.unicauca.microserviciousuarios.domain.model.User;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.documents.UserDocument;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.mappers.UserMapper;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.mongoRepositories.MongoRespositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User createUser(User userToCreate){
        userToCreate.setId(null);
        UserDocument userDocument = UserMapper.toUserDocument(userToCreate);
        UserDocument createdUser = mongoRepository.save(userDocument);
        return UserMapper.toUser(createdUser);
    }
    @Override
    public User findUserById(String id){
        UserDocument userFounded = mongoRepository.findById(id).orElse(null);
        if(userFounded==null || !userFounded.isActive())
            return null;
        return UserMapper.toUser(userFounded);
    }
    @Override
    public User updateUserById(String id, User newUser){
        Optional<UserDocument> userOptional = mongoRepository.findById(id);

        if(userOptional.isEmpty())
            throw new RuntimeException("User not found");

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

        UserDocument updatedUser = mongoRepository.save(userDocument);
        return UserMapper.toUser(updatedUser);
    }
    @Override
    public User deleteUserById(String id){
        Optional<UserDocument> userOptional = mongoRepository.findById(id);

        if(userOptional.isEmpty())
            throw new RuntimeException("User not found");

        UserDocument userDocument = userOptional.get();
        userDocument.setActive(false);

        UserDocument userDeleted = mongoRepository.save(userDocument);

        return UserMapper.toUser(userDeleted);
    }
    @Override
    public User loginUser(String email, String password ){
        Query query = new Query();

        query.addCriteria(Criteria.where("email").is(email).and("password").is(password));

        UserDocument userDocument = mongoTemplate.findOne(query, UserDocument.class);

        if(userDocument == null)
            throw new RuntimeException("Invalid email and password");
        return UserMapper.toUser(userDocument);
    }
}
