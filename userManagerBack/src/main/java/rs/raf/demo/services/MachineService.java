package rs.raf.demo.services;

import rs.raf.demo.dto.SearchParamsDto;
import rs.raf.demo.model.Machine;
import rs.raf.demo.dto.MachineDto;

import java.util.List;
import java.util.Optional;

public interface MachineService {

    public Machine createMachine(MachineDto machineDto);

    public void destroyMachine(long id);

    public void startMachine(long id);

    public void stopMachine(long id);

    public void restartMachine(long id);

    public List<Machine> searchMachines(SearchParamsDto searchParamsDto);



}
