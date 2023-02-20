package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByUsername(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }

        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), new ArrayList<>());
    }


    //domaci3 deo

    public ResponseEntity<?> findAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = this.userRepository.findByUsername(email);
        boolean isThere = false;

        for (Permission p : user.getPermissions()) {
            if (p.getName().equals("can_read_users")) {
                isThere = true;
                break;
            }
        }

        if (!isThere) {
            return ResponseEntity.status(401).build();
        }

        List<User> users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<?> createNewUser(User user) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        User user1 = this.userRepository.findByUsername(email);
        boolean isThere = false;

        for (Permission p : user1.getPermissions()) {
            if (p.getName().equals("can_create_users")) {
                isThere = true;
                break;
            }
        }

        if (!isThere) {
            return ResponseEntity.status(403).build();
        }

        if (this.userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(422).build();
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(user);

        return ResponseEntity.ok().build();


    }

    public ResponseEntity<?> updateUser(User user, int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user1 = this.userRepository.findByUsername(email);
        boolean isThere = false;

        for (Permission p : user1.getPermissions()) {
            if (p.getName().equals("can_update_users")) {
                isThere = true;
                break;
            }
        }



        if (!isThere) {
            return ResponseEntity.status(403).build();
        }

        User user2 = this.userRepository.findById(id);

        if (user2 == null) {
            return ResponseEntity.status(422).build();
        }

        if (user.getUsername() != null) {

            if (user.getUsername().equals(user1.getUsername())) {
                return ResponseEntity.status(422).build();
            }

            if (this.userRepository.existsByUsername(user.getUsername()))
                return ResponseEntity.status(422).build();
            user2.setUsername(user.getUsername());
        }
        if (user.getName() != null)
            user2.setName(user.getName());
        if (user.getSurname() != null)
            user2.setSurname(user.getSurname());
        if (user.getPassword() != null)
            user2.setPassword(passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(user2);

        return ResponseEntity.ok(user2);
    }

    public ResponseEntity<?> deleteUser(int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user1 = this.userRepository.findByUsername(email);
        boolean isThere = false;


        for (Permission p : user1.getPermissions()) {
            if (p.getName().equals("can_delete_users")) {
                isThere = true;
                break;
            }
        }


        if (!isThere) {
            return ResponseEntity.status(403).build();
        }

        this.userRepository.deleteById(id);

        return ResponseEntity.ok().build();

    }

}
