package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.MachineDto;
import rs.raf.demo.dto.SearchParamsDto;
import rs.raf.demo.exceptions.ForbiddenException;
import rs.raf.demo.exceptions.MachineBusyException;
import rs.raf.demo.exceptions.MachineStatusException;
import rs.raf.demo.model.Machine;
import rs.raf.demo.services.MachineService;

import javax.persistence.EntityNotFoundException;
import javax.websocket.server.PathParam;

@RestController
@CrossOrigin
@RequestMapping("/machine")
public class MachineController {

    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Machine> createMachine(@RequestBody MachineDto machineDto) {
        try {
            return new ResponseEntity<>(this.machineService.createMachine(machineDto),HttpStatus.OK);
        }
        catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable("id") long id) {
        try {
            this.machineService.destroyMachine(id);
            return ResponseEntity.ok().build();
        }
        catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (MachineStatusException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (MachineBusyException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }
    }

    @PutMapping(value = "/start/{id}")
    public ResponseEntity<?> startMachine(@PathVariable("id") long id) {
        try {
            this.machineService.startMachine(id);
            return ResponseEntity.ok().build();
        }catch (MachineBusyException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }catch (ForbiddenException e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build();
        }catch (MachineStatusException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping(value = "/stop/{id}")
    public ResponseEntity<?> stopMachine(@PathVariable("id") long id) {
        try {
            this.machineService.stopMachine(id);
            return ResponseEntity.ok().build();
        }catch (MachineBusyException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }catch (ForbiddenException e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build();
        }catch (MachineStatusException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping(value = "/restart/{id}")
    public ResponseEntity<?> restartMachine(@PathVariable("id") long id) {
        try {
            this.machineService.restartMachine(id);
            return ResponseEntity.ok().build();
        }catch (MachineBusyException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }catch (ForbiddenException e) {
            e.printStackTrace();
            return ResponseEntity.status(403).build();
        }catch (MachineStatusException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(value = "/search")
    public ResponseEntity<?> searchMachines(@RequestBody SearchParamsDto searchParamsDto) {
        return ResponseEntity.ok().body(this.machineService.searchMachines(searchParamsDto));
    }












}
