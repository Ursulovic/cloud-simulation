package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.dto.MachineDto;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.services.MachineService;

import java.util.Date;

@Service
@Transactional
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    private final UserRepository userRepository;

    @Autowired
    public MachineServiceImpl(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Machine createMachine(MachineDto machineDto) throws AuthenticationException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!checkPermission(email, "can_create_machines")) {
            throw new AccountExpiredException("You do not have a permission for this action!");
        }

        Machine machine = new Machine();
        machine.setCreator(this.userRepository.findByUsername(email));
        machine.setStatus(MachineStatus.STOPPED.toString());
        machine.setActive(machineDto.isActive());
        machine.setCreationDate(new Date().getTime());

        this.machineRepository.save(machine);

        return machine;
    }

    @Override
    public void destroyMachine(long id) throws AuthenticationException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!checkPermission(email, "can_destroy_machines")) {
            throw new AccountExpiredException("You do not have a permission for this action!");
        }
        this.machineRepository.removeMachineById(id);
    }


    private boolean checkPermission(String email, String permission) {
        User user = this.userRepository.findByUsername(email);
        boolean isThere = false;
        for (Permission p : user.getPermissions()) {
            if (p.getName().equals(permission)) {
                isThere = true;
                break;
            }
        }
        return isThere;
    }

}
