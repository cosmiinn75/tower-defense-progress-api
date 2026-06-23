package com.cosmin.tower_defense_progress_api.playerProgress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerProgressRepository extends JpaRepository<PlayerProgress,Long> {
    Optional<PlayerProgress> findByUserUsername(String username);
}
