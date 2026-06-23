package com.cosmin.tower_defense_progress_api.levelProgress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelProgressRepository extends JpaRepository<LevelProgress,Long> {
    List<LevelProgress> findByUserUsernameOrderByLevelNumberAsc(String username);
    Optional<LevelProgress> findByUserUsernameAndLevelNumber(String username,Integer levelNumber);

}
