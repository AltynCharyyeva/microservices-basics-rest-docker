package mcs_person.dtos;

import lombok.Data;

import java.util.UUID;
@Data
public class LoginDTO {

    private String name;
    private String password;

    public LoginDTO(){

    }
    public LoginDTO(String name, String password){
        this.name = name;
        this.password = password;
    }
}
