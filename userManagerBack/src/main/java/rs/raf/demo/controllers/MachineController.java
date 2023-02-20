package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.MachineDto;
import rs.raf.demo.exceptions.ForbiddenException;
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
    }

    @PutMapping(value = "/start/{id}")
    public ResponseEntity<?> startMachine(@PathVariable("id") long id) {
        try {
            this.machineService.startMachine(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(505).build();
        }
    }

}
