package com.cosmin.tower_defense_progress_api.progress;

import com.cosmin.tower_defense_progress_api.dto.LevelProgressResponse;
import com.cosmin.tower_defense_progress_api.dto.PlayerProgressResponse;
import com.cosmin.tower_defense_progress_api.dto.UpdateLevelRequest;
import com.cosmin.tower_defense_progress_api.exception.*;
import com.cosmin.tower_defense_progress_api.levelProgress.LevelProgress;
import com.cosmin.tower_defense_progress_api.levelProgress.LevelProgressRepository;
import com.cosmin.tower_defense_progress_api.playerProgress.PlayerProgress;
import com.cosmin.tower_defense_progress_api.playerProgress.PlayerProgressRepository;

import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.util.List;


@Service
public class ProgressService {
    private final LevelProgressRepository levelProgressRepository;
    private final PlayerProgressRepository playerProgressRepository;


    public ProgressService(LevelProgressRepository levelProgressRepository, PlayerProgressRepository playerProgressRepository) {
        this.levelProgressRepository = levelProgressRepository;
        this.playerProgressRepository = playerProgressRepository;
    }

    public PlayerProgressResponse getProgress(){
        String username = getCurrentUsername();

        PlayerProgress playerProgress = playerProgressRepository.findByUserUsername(username)
                .orElseThrow(()-> new PlayerProgressNotFoundException("Player progress not found"));

        List<LevelProgress> levels = levelProgressRepository.findByUserUsernameOrderByLevelNumberAsc(username);

        return fromPlayerToResponse(playerProgress,levels);

    }

    public PlayerProgressResponse updateLevel(Integer levelNumber, UpdateLevelRequest updateLevelRequest){
        String username = getCurrentUsername();

        if(levelNumber < 1 || levelNumber > 10){
            throw new InvalidLevelException("Level number must be between 1 and 10");
        }

        PlayerProgress playerProgress = playerProgressRepository.findByUserUsername(username)
                .orElseThrow(() -> new PlayerProgressNotFoundException("Player progress not found"));

        if(levelNumber > playerProgress.getMaxLevelUnlocked()) {
            throw new LevelLockedException("Level is locked");
        }

        LevelProgress levelProgress = levelProgressRepository.findByUserUsernameAndLevelNumber(username,levelNumber)
                .orElseThrow(() -> new LevelProgressNotFoundException("Level progress not found"));

        if(updateLevelRequest.stars() > levelProgress.getStarUnlocked()) {
            levelProgress.setStarUnlocked(updateLevelRequest.stars());
            levelProgressRepository.save(levelProgress);
        }

        if(levelNumber.equals(playerProgress.getMaxLevelUnlocked()) && playerProgress.getMaxLevelUnlocked() < 10) {
            playerProgress.setMaxLevelUnlocked(playerProgress.getMaxLevelUnlocked() + 1);
            playerProgressRepository.save(playerProgress);
        }
        List<LevelProgress> levels = levelProgressRepository.findByUserUsernameOrderByLevelNumberAsc(username);
        return fromPlayerToResponse(playerProgress,levels);

    }

    @Transactional
    public PlayerProgressResponse resetProgress(){
        String username = getCurrentUsername();
        PlayerProgress playerProgress = playerProgressRepository.findByUserUsername(username)
                .orElseThrow(() -> new PlayerProgressNotFoundException("Player progress not found"));

        playerProgress.setMaxLevelUnlocked(1);
        List<LevelProgress> levels = levelProgressRepository.findByUserUsernameOrderByLevelNumberAsc(username);

        for(LevelProgress levelProgress : levels){
            levelProgress.setStarUnlocked(0);
        }
        playerProgressRepository.save(playerProgress);
        levelProgressRepository.saveAll(levels);
        return fromPlayerToResponse(playerProgress,levels);
    }



    private PlayerProgressResponse fromPlayerToResponse(PlayerProgress playerProgress , List<LevelProgress> levels) {

        List<LevelProgressResponse> responses = levels.stream()
                .map(level -> new LevelProgressResponse(
                        level.getLevelNumber(),level.getStarUnlocked()
                ))
                .toList();
        return new PlayerProgressResponse(playerProgress.getMaxLevelUnlocked(), responses);
    }


    private String  getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
