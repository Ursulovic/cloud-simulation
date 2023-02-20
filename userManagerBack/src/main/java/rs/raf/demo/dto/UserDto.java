package rs.raf.demo.dto;

import lombok.Data;
import rs.raf.demo.model.User;

import java.util.List;

@Data
public class UserDto {

    private List<Integer> permissions;

    private String name;

    private String surname;

    private String email;

    private String password;

}
