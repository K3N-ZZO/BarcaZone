package com.barcazone.controller;


import com.barcazone.dto.AuthResponse;
import com.barcazone.dto.LoginRequest;
import com.barcazone.dto.RegisterRequest;
import com.barcazone.entity.Role;
import com.barcazone.entity.User;
import com.barcazone.repository.RoleRepository;
import com.barcazone.repository.UserRepository;
import com.barcazone.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthenticationManager am;
    private final JwtService jwt;
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder pe;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        if(users.findByUsername(req.getUsername()).isPresent()) return ResponseEntity.badRequest().body("Username taken");
        Role r = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role(null,"ROLE_USER")));
        User u=new User(); u.setUsername(req.getUsername()); u.setPassword(pe.encode(req.getPassword())); u.getRoles().add(r);
        users.save(u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        try{
            am.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            String token=jwt.generate(req.getUsername());
            AuthResponse resp=new AuthResponse(); resp.setAccessToken(token); resp.setExpiresIn(120*60);
            return ResponseEntity.ok(resp);
        }catch(AuthenticationException e){ return ResponseEntity.status(401).body("Invalid credentials"); }
    }
}
