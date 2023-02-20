package rs.raf.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.UserDto;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.PermissionRepository;

@Service
public class UserMapper {

    private final PermissionRepository permissionRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserMapper(PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
    }


    public User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setUsername(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        for (Integer i : userDto.getPermissions()) {
            Permission permission = permissionRepository.findPermissionById(i);
            user.getPermissions().add(permission);
        }
        return user;
    }
}
