package rs.raf.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.LoginRequest;
import rs.raf.demo.responses.LoginResponse;
import rs.raf.demo.services.impl.UserServiceImpl;
import rs.raf.demo.utils.JwtUtil;
//import rs.edu.raf.spring_project.model.AuthReq;
//import rs.edu.raf.spring_project.model.AuthRes;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userServiceImpl = userServiceImpl;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception   e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername())));
    }


}
