package infrastructure.repositories.mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.model.IUserRepository;
import domain.model.User;
import infrastructure.repositories.mongoDB.documents.UserDocument;
import infrastructure.repositories.mongoDB.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public class UserRepositoyMongo implements IUserRepository {

    private final MongoRepository<UserDocument, String> mongoRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    public UserRepositoyMongo(MongoRepository<UserDocument, String> mongoRepository, MongoTemplate mongoTemplate){
        this.mongoRepository = mongoRepository;
        this.mongoTemplate = mongoTemplate;
    }



    /**
     * @param userToCreate add new user in the database
     * @return userCreated
     */
    @Override
    public User createUser(User userToCreate){
        UserDocument userDocument = UserMapper.toUserDocument(userToCreate);
        UserDocument createdUser = mongoRepository.save(userDocument);
        return UserMapper.toUser(createdUser);
    }
    @Override
    public User findUserById(String id){
        UserDocument userFounded = mongoRepository.findById(id).orElse(null);
        return UserMapper.toUser(userFounded);
    }
    @Override
    public User updateUserById(String id, User newUser){
        Optional<UserDocument> userOptional = mongoRepository.findById(id);

        if(!userOptional.isPresent())
            throw new RuntimeException("User not found");

        UserDocument userDocument = userOptional.get();

        if(!newUser.getName().equals(""))
            userDocument.setName(newUser.getName());
        if(!newUser.getEmail().equals(""))
            userDocument.setEmail(newUser.getEmail());
        if(!newUser.getPassword().equals(""))
            userDocument.setPassword(newUser.getPassword());
        if(newUser.getAddress().equals(""))
            userDocument.setAddress(newUser.getAddress());
        if(newUser.getPhone() != 0)
            userDocument.setPhone(newUser.getPhone());

        UserDocument updatedUser = mongoRepository.save(userDocument);
        return UserMapper.toUser(updatedUser);
    };
    @Override
    public User deleteUserById(String id){
        Optional<UserDocument> userOptional = mongoRepository.findById(id);

        if(!userOptional.isPresent())
            throw new RuntimeException("User not found");

        mongoRepository.deleteById(id);

        return UserMapper.toUser(userOptional.get());
    }
    @Override
    public User loginUser(String email, String password ){
        Query query = new Query();

        query.addCriteria(Criteria.where("email").is(email).and("paddword").is(password));

        UserDocument userDocument = mongoTemplate.findOne(query, UserDocument.class);

        if(userDocument == null)
            throw new RuntimeException("Invalid email and password");
        return UserMapper.toUser(userDocument);
    }
}
