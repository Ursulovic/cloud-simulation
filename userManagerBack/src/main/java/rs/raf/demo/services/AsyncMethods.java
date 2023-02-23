package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.exceptions.MachineBusyException;
import rs.raf.demo.exceptions.MachineStatusException;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.machine.MachineOperation;
import rs.raf.demo.model.machine.MachineStatus;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.MachineRepository;

import java.util.Date;
import java.util.Random;

@Service
@EnableAsync
public class AsyncMethods {

    private final MachineRepository machineRepository;

    private final ErrorMessageRepository errorMessageRepository;

    @Autowired
    public AsyncMethods(MachineRepository machineRepository, ErrorMessageRepository errorMessageRepository) {
        this.machineRepository = machineRepository;
        this.errorMessageRepository = errorMessageRepository;
    }

    @Async
    @Transactional
    public void setStatus(Machine machine , int time, MachineStatus machineStatus) {

        long time1 = new Date().getTime();

        System.out.println("Machine " + machine.getId() + " is being set to " + machineStatus.toString() + " status...");

        try {
            Thread.sleep(10_000 + time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (machineStatus == MachineStatus.RUNNING)
            machine.setStatus(MachineStatus.RUNNING.toString());
        else
            machine.setStatus(MachineStatus.STOPPED.toString());
        machine.setBusy(false);
        machineRepository.save(machine);
        //zasto ne radi .flush()?

        long time2 = new Date().getTime();

        System.out.println("Execution time: " + (double)(time2 - time1) /1000);
    }

    @Async
    public void restart(Machine machine, int time) {
        System.out.println("Machine " + machine.getId() + "is being restarted...");

        try {
            Thread.sleep((10_000 + time) / 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        machine.setStatus(MachineStatus.STOPPED.toString());
        machineRepository.save(machine);
        System.out.println("Machine turned off");
        try {
            Thread.sleep((10_000 + time) / 2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        machine.setStatus(MachineStatus.RUNNING.toString());
        machineRepository.save(machine);
        System.out.println("Machine turned on");

    }

    @Async
    @Transactional
    public void scheduleOperation(Machine machine, long date, MachineOperation machineOperation) {

        switch (machineOperation) {
            case START:
                if (!machine.getStatus().equals(MachineStatus.STOPPED.toString())) {
                    logError(machine, new Date().getTime(), machineOperation, "Machine already running");
                }
                synchronized (this) {
                    if (!machine.isBusy()) {
                        machine.setBusy(true);
                        this.machineRepository.save(machine);
                        setStatus(machine, new Random().nextInt(5000), MachineStatus.RUNNING);
                    } else
                        logError(machine, new Date().getTime(), machineOperation, "Machine busy");
                }
                break;
            case STOP:
                if (!machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
                    logError(machine, new Date().getTime(), machineOperation, "Machine already stopped");
                }
                synchronized (this) {
                    if (!machine.isBusy()) {
                        machine.setBusy(true);
                        this.machineRepository.save(machine);
                        setStatus(machine, new Random().nextInt(5000), MachineStatus.STOPPED);
                    } else
                        logError(machine, new Date().getTime(), machineOperation, "Machine busy");
                }
                break;
            case RESTART:
                if (!machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
                    logError(machine, new Date().getTime(), machineOperation, "Machine is not running");
                }
                synchronized (this) {
                    if (!machine.isBusy()) {
                        machine.setBusy(true);
                        this.machineRepository.save(machine);
                        restart(machine, new Random().nextInt(5000));
                    } else {
                        throw new MachineBusyException();
                    }
                }


        }

    }

    public void logError(Machine machine, long date, MachineOperation machineOperation, String message) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMachine(machine);
        errorMessage.setDate(date);
        errorMessage.setOperation(machineOperation.toString());
        errorMessage.setMessage(message);
        this.errorMessageRepository.save(errorMessage);
    }
}
