package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.MachineDto;
import rs.raf.demo.model.Machine;
import rs.raf.demo.services.MachineService;

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
        catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable("id") long id) {
        System.out.println("AAAAAAAAAAAA");
        System.out.println(id);
        try {
            this.machineService.destroyMachine(id);
            return ResponseEntity.ok().build();
        }
        catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
