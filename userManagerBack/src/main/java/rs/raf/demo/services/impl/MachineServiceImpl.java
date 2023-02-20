package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import rs.raf.demo.exceptions.ForbiddenException;
import rs.raf.demo.exceptions.MachineStatusException;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.dto.MachineDto;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.services.MachineService;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Random;

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
    public Machine createMachine(MachineDto machineDto) throws ForbiddenException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!checkPermission(email, "can_create_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
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
    public void destroyMachine(long id) throws ForbiddenException, EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!checkPermission(email, "can_destroy_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }
        Machine machine = this.machineRepository.findMachineById(id);


        System.out.println("AAAAAAA");
        System.out.println(id);

        if (machine == null) {
            throw new EntityNotFoundException("No machine with provied id");
        }

        if (machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
            throw new MachineStatusException("Machine is not active");
        }

        machine.setActive(false);
        this.machineRepository.flush();

    }

    @Override
    public void startMachine(long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!checkPermission(email, "can_start_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }

        Machine machine = this.machineRepository.findMachineById(id);



        if (!machine.getStatus().equals(MachineStatus.STOPPED.toString())) {
            throw new MachineStatusException("Machine already running");
        }

        _startMachine(machine, new Random().nextInt(5000));




    }


    //async methods


    @Async
    public void _startMachine(Machine machine ,int time) {

        long time1 = new Date().getTime();

        try {
            Thread.sleep(10_000 + time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        machine.setStatus(MachineStatus.RUNNING.toString());
        this.machineRepository.flush();

        long time2 = new Date().getTime();

        System.out.println("Execution time: " + (double)(time2 - time1) /1000);

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
