package rs.raf.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.dto.ScheduleOperationDto;
import rs.raf.demo.dto.SearchParamsDto;
import rs.raf.demo.exceptions.ForbiddenException;
import rs.raf.demo.exceptions.MachineBusyException;
import rs.raf.demo.exceptions.MachineStatusException;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.machine.MachineOperation;
import rs.raf.demo.model.machine.MachineStatus;
import rs.raf.demo.model.Permission;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.dto.MachineDto;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.services.AsyncMethods;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.ScheduleOperation;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@EnableAsync
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    private final UserRepository userRepository;

    private final AsyncMethods asyncMethods;

    private final ScheduleOperation scheduleOperation;

    @Autowired
    public MachineServiceImpl(MachineRepository machineRepository, UserRepository userRepository, AsyncMethods asyncMethods, ScheduleOperation scheduleOperation) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.asyncMethods = asyncMethods;
        this.scheduleOperation = scheduleOperation;
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
        machine.setBusy(false);
        machine.setName(machineDto.getName());

        this.machineRepository.save(machine);

        return machine;
    }

    @Override
    @Transactional
    public void destroyMachine(long id) throws ForbiddenException, EntityNotFoundException, MachineStatusException, MachineBusyException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!checkPermission(email, "can_destroy_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }
        Machine machine = this.machineRepository.findMachineById(id);



        if (machine == null) {
            throw new EntityNotFoundException("No machine with provied id");
        }

        if (machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
            throw new MachineStatusException("Machine is not active");
        }

        if (machine.isBusy())
            throw new MachineBusyException();

        machine.setActive(false);
        this.machineRepository.save(machine);

    }

    @Override
    @Transactional
    public void startMachine(long id) throws MachineStatusException, MachineBusyException, ForbiddenException, EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!checkPermission(email, "can_start_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }

            Machine machine = this.machineRepository.findMachineById(id);

            if (!machine.getStatus().equals(MachineStatus.STOPPED.toString())) {
                throw new MachineStatusException("Machine already running");
            }

        synchronized (this) {
            if (!machine.isBusy()) {
                machine.setBusy(true);
                this.machineRepository.save(machine);
                asyncMethods.setStatus(machine, new Random().nextInt(5000), MachineStatus.RUNNING);
            } else {
                throw new MachineBusyException();
            }
        }

    }

    @Override
    @Transactional
    public void stopMachine(long id) throws MachineStatusException, MachineBusyException, ForbiddenException, EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!checkPermission(email, "can_stop_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }

        Machine machine = this.machineRepository.findMachineById(id);


        if (!machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
            throw new MachineStatusException("Machine is already stopped");
        }



        synchronized (this) {
            if (!machine.isBusy()) {
                machine.setBusy(true);
                this.machineRepository.save(machine);
                asyncMethods.setStatus(machine, new Random().nextInt(5000), MachineStatus.STOPPED);
            } else {
                throw new MachineBusyException();
            }
        }
    }

    @Override
    public void restartMachine(long id) throws MachineStatusException, MachineBusyException, ForbiddenException, EntityNotFoundException  {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!checkPermission(email, "can_restart_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }

        Machine machine = this.machineRepository.findMachineById(id);

        if (!machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
            throw new MachineStatusException("Machine is not running");
        }

        synchronized (this) {
            if (!machine.isBusy()) {
                machine.setBusy(true);
                this.machineRepository.save(machine);
                asyncMethods.restart(machine, new Random().nextInt(5000));
            } else {
                throw new MachineBusyException();
            }
        }




    }



    @Override
    public List<Machine> searchMachines(SearchParamsDto searchParamsDto) throws MachineStatusException, MachineBusyException, ForbiddenException, EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!checkPermission(email, "can_search_machines")) {
            throw new ForbiddenException("You do not have a permission for this action!");
        }



        List<Machine> machines = new ArrayList<>();

        machines = this.machineRepository.searchMachines(searchParamsDto.getName(),
                searchParamsDto.getStatus(),
                searchParamsDto.getDateFrom(),
                searchParamsDto.getDateTo());


        return machines;
    }

    @Override
    public void scheduleOperation(ScheduleOperationDto scheduleOperationDto) throws RequestRejectedException, ForbiddenException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean b = false;

        System.out.println(scheduleOperationDto.toString());

        switch (scheduleOperationDto.getOperation().toUpperCase()) {
            case "START":
                b = checkPermission(email, "can_start_machines");
                break;
            case "STOP":
                b = checkPermission(email, "can_stop_machines");
                break;
            case "RESTART":
                b = checkPermission(email, "can_restart_machines");
                break;
            default:
                throw new RequestRejectedException("Operation not supported");
        }

        if (!b)
            throw new ForbiddenException("Not permitted");

        Machine machine = this.machineRepository.findMachineById(scheduleOperationDto.getId());




        scheduleOperation.scheduleOperation(machine, scheduleOperationDto.getDate(), MachineOperation.valueOf(scheduleOperationDto.getOperation()));



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
