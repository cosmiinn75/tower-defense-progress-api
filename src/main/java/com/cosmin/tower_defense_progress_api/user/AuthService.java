package com.cosmin.tower_defense_progress_api.user;

import com.cosmin.tower_defense_progress_api.dto.AuthRequest;
import com.cosmin.tower_defense_progress_api.dto.AuthResponse;
import com.cosmin.tower_defense_progress_api.levelProgress.LevelProgress;
import com.cosmin.tower_defense_progress_api.levelProgress.LevelProgressRepository;
import com.cosmin.tower_defense_progress_api.playerProgress.PlayerProgress;
import com.cosmin.tower_defense_progress_api.playerProgress.PlayerProgressRepository;
import com.cosmin.tower_defense_progress_api.security.JWTUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PlayerProgressRepository playerProgressRepository;
    private final LevelProgressRepository levelProgressRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jWTUtil;

    public AuthService(UserRepository userRepository, PlayerProgressRepository playerProgressRepository, LevelProgressRepository levelProgressRepository, PasswordEncoder passwordEncoder, JWTUtil jWTUtil) {
        this.userRepository = userRepository;
        this.playerProgressRepository = playerProgressRepository;
        this.levelProgressRepository = levelProgressRepository;
        this.passwordEncoder = passwordEncoder;
        this.jWTUtil = jWTUtil;
    }

    public AuthResponse loginUser(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(()-> new RuntimeException("Invalid username or password"));
        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            throw new RuntimeException("Invalid username or password");
        }

        return new AuthResponse(jWTUtil.generateToken(user.getUsername()));
    }

    @Transactional
    public AuthResponse registerUser(AuthRequest request){

        Optional<User> userOptional = userRepository.findByUsername(request.username());

        if(userOptional.isPresent()){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUsername(request.username());
        User savedUser = userRepository.save(user);
        PlayerProgress playerProgress = new PlayerProgress();
        playerProgress.setMaxLevelUnlocked(1);
        playerProgress.setUser(savedUser);
        playerProgressRepository.save(playerProgress);

        for(int i = 1 ; i <= 10 ; i++){
            LevelProgress levelProgress = new LevelProgress();
            levelProgress.setLevelNumber(i);
            levelProgress.setStarUnlocked(0);
            levelProgress.setUser(savedUser);
            levelProgressRepository.save(levelProgress);
        }

        return new AuthResponse(jWTUtil.generateToken(savedUser.getUsername()));
    }

}
