package co.edu.unicauca.microserviciousuarios.domain.model;

import co.edu.unicauca.microserviciousuarios.domain.model.exceptions.InvalidUserInformation;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String rol;
    private int phone;

    public User(String id, String name, String email, String password, String address, String rol, int phone) throws InvalidUserInformation {
        validateName(name);
        validateEmail(email);
        validatePhone(phone);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.rol = rol;
        this.phone = phone;
    }
    public User(){

    }

    private void validateName(String name) throws InvalidUserInformation {
        if(name == null || name.isEmpty()){
            throw new InvalidUserInformation("Name is null or empty");
        }
    }

    private void validatePhone(int phone) throws InvalidUserInformation {
        if(phone < 0){
            throw new InvalidUserInformation("Phone number is negative");
        }
    }

    private void validateEmail(String email) throws InvalidUserInformation {
        if(email == null || email.isEmpty()){
            throw new InvalidUserInformation("Email is null or empty");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
         return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getRol() {return rol;}

    public int getPhone() {
        return phone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) throws InvalidUserInformation {
        validateName(name);
        this.name = name;
    }

    public void setEmail(String email) throws InvalidUserInformation {
        validateEmail(email);
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRol(String rol) {this.rol = rol;}

    public void setPhone(int phone) throws InvalidUserInformation {
        validatePhone(phone);
        this.phone = phone;
    }
}
