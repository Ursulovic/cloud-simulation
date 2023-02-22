package rs.raf.demo.services;

import rs.raf.demo.model.Machine;
import rs.raf.demo.dto.MachineDto;

import java.util.List;

public interface MachineService {

    public Machine createMachine(MachineDto machineDto);

    public void destroyMachine(long id);

    public void startMachine(long id);

    public void stopMachine(long id);

    public void restartMachine(long id);






}
