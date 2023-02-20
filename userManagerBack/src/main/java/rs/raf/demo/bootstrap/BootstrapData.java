package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.repositories.*;

@Component
public class BootstrapData implements CommandLineRunner {


    private final UserRepository userRepository;

    private final PermissionRepository permissionRepository;

    private final MachineRepository machineRepository;

    private final PasswordEncoder passwordEncoder;



    @Autowired
    public BootstrapData(UserRepository userRepository, PermissionRepository permissionRepository, MachineRepository machineRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.machineRepository = machineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        Permission p1 = new Permission();
        p1.setName("can_read_users");

        Permission p2 = new Permission();
        p2.setName("can_create_users");

        Permission p3 = new Permission();
        p3.setName("can_update_users");

        Permission p4 = new Permission();
        p4.setName("can_delete_users");

        //projekat deo

        Permission p5 = new Permission();
        p5.setName("can_search_machines");

        Permission p6 = new Permission();
        p6.setName("can_start_machines");

        Permission p7 = new Permission();
        p7.setName("can_stop_machines");

        Permission p8 = new Permission();
        p8.setName("can_restart_machines");

        Permission p9 = new Permission();
        p9.setName("can_create_machines");

        Permission p10 = new Permission();
        p10.setName("can_destroy_machines");

        this.permissionRepository.save(p1);
        this.permissionRepository.save(p2);
        this.permissionRepository.save(p3);
        this.permissionRepository.save(p4);
        this.permissionRepository.save(p5);
        this.permissionRepository.save(p6);
        this.permissionRepository.save(p7);
        this.permissionRepository.save(p8);
        this.permissionRepository.save(p9);
        this.permissionRepository.save(p10);





        User user1 = new User();
        user1.setUsername("ivan@gmail.com");
        user1.setPassword(this.passwordEncoder.encode("123"));
        user1.setName("Ivan");
        user1.setSurname("Ivanovic");

        this.userRepository.save(user1);


        // ????????????????????????????
        user1.getPermissions().add(p1);
        user1.getPermissions().add(p2);
        user1.getPermissions().add(p3);
        user1.getPermissions().add(p4);
        user1.getPermissions().add(p5);
        user1.getPermissions().add(p6);
        user1.getPermissions().add(p7);
        user1.getPermissions().add(p8);
        user1.getPermissions().add(p9);
        user1.getPermissions().add(p10);

        this.userRepository.save(user1);





        System.out.println("Data loaded!");
    }
}
