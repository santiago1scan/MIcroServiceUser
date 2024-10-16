package co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.mongoRepositories;

import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRespositoryUser extends MongoRepository<UserDocument, String> {
}
