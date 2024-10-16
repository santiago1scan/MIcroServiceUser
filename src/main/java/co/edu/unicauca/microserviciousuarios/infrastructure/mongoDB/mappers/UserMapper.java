package co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.mappers;

import co.edu.unicauca.microserviciousuarios.domain.model.User;
import co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.documents.UserDocument;

public class UserMapper {
    public static User toUser(UserDocument userD){
        return new User(
                userD.getId(),
                userD.getName(),
                userD.getEmail(),
                userD.getPassword(),
                userD.getAddress(),
                userD.getPhone()
        );
    }
    public static UserDocument toUserDocument(User user){
        return new UserDocument(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getAddress(),
            user.getPhone()
        );
    }
}
