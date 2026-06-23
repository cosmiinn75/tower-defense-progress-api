package com.cosmin.tower_defense_progress_api.user;

import com.cosmin.tower_defense_progress_api.dto.AuthRequest;
import com.cosmin.tower_defense_progress_api.dto.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse registerUser( @Valid @RequestBody AuthRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse loginUser( @Valid @RequestBody AuthRequest request){
        return authService.loginUser(request);
    }

}
