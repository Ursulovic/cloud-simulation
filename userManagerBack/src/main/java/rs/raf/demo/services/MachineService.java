package rs.raf.demo.services;

import rs.raf.demo.model.Machine;
import rs.raf.demo.dto.MachineDto;

public interface MachineService {

    public Machine createMachine(MachineDto machineDto);

    public void destroyMachine(long id);

    public void startMachine(long id);





}
