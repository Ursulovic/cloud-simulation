package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.MachineStatus;
import rs.raf.demo.repositories.MachineRepository;

import java.util.Date;

@Service
@EnableAsync
public class AsyncMethods {

    @Autowired
    public AsyncMethods() {
    }

    @Async
    @Transactional
    public void _startMachine(Machine machine , int time, MachineRepository machineRepository) {

        long time1 = new Date().getTime();

        try {
            Thread.sleep(10_000 + time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        machine.setStatus(MachineStatus.RUNNING.toString());
        machineRepository.save(machine);
        //zasto ne radi .flush()?

        long time2 = new Date().getTime();

        System.out.println("Execution time: " + (double)(time2 - time1) /1000);
    }


}
