package co.edu.unicauca.microserviciousuarios.infrastructure.mongoDB.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="users")
@Setter
@Getter
@AllArgsConstructor
public class UserDocument {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String address;
    private int phone;


    
    public UserDocument(){

    }

}
