package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.repositories.MachineRepository;

import javax.persistence.LockModeType;
import java.util.Date;

@Service
@EnableAsync
public class AsyncMethods {

    private final MachineRepository machineRepository;

    @Autowired
    public AsyncMethods(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
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



}
