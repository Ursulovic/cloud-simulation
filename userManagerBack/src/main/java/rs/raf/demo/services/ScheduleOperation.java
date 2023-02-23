package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.exceptions.MachineBusyException;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.machine.MachineOperation;
import rs.raf.demo.model.machine.MachineStatus;
import rs.raf.demo.repositories.MachineRepository;

import java.util.Date;
import java.util.Random;

@Service
@EnableAsync
public class ScheduleOperation {

    private final AsyncMethods asyncMethods;

    private final MachineRepository machineRepository;

    @Autowired
    public ScheduleOperation(AsyncMethods asyncMethods, MachineRepository machineRepository) {
        this.asyncMethods = asyncMethods;
        this.machineRepository = machineRepository;
    }

    @Async
    @Transactional
    public void scheduleOperation(Machine machine, long date, MachineOperation machineOperation) {

        long now = new Date().getTime() / 1000;

        //test

        if (date <= now) {
            throw new RuntimeException("Bad entry");
        }

        System.out.println("Date: " + date);
        System.out.println("Now: " + now);

        System.out.println(date - now);

        try {
            Thread.sleep(date - now);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        switch (machineOperation) {
            case START:
                if (!machine.getStatus().equals(MachineStatus.STOPPED.toString())) {
                    asyncMethods.logError(machine, new Date().getTime(), machineOperation, "Machine already running");
                    break;
                }
                synchronized (this) {
                    if (!machine.isBusy()) {
                        machine.setBusy(true);
                        this.machineRepository.save(machine);
                        asyncMethods.setStatus(machine, new Random().nextInt(5000), MachineStatus.RUNNING);
                    } else
                        asyncMethods.logError(machine, new Date().getTime(), machineOperation, "Machine busy");
                }
                break;
            case STOP:
                if (!machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
                    asyncMethods.logError(machine, new Date().getTime(), machineOperation, "Machine already stopped");
                    break;
                }
                synchronized (this) {
                    if (!machine.isBusy()) {
                        machine.setBusy(true);
                        this.machineRepository.save(machine);
                        asyncMethods.setStatus(machine, new Random().nextInt(5000), MachineStatus.STOPPED);
                    } else
                        asyncMethods.logError(machine, new Date().getTime(), machineOperation, "Machine busy");
                }
                break;
            case RESTART:
                if (!machine.getStatus().equals(MachineStatus.RUNNING.toString())) {
                    asyncMethods.logError(machine, new Date().getTime(), machineOperation, "Machine is not running");
                    break;
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
                break;

        }

    }

}
