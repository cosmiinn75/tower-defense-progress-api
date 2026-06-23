package com.cosmin.tower_defense_progress_api.playerProgress;

import com.cosmin.tower_defense_progress_api.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "player_progress")
public class PlayerProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maxLevelUnlocked = 1;

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    private User user;

    public PlayerProgress(Long id, Integer maxLevelUnlocked, User user) {
        this.id = id;
        this.maxLevelUnlocked = maxLevelUnlocked;
        this.user = user;
    }

    public PlayerProgress() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxLevelUnlocked() {
        return maxLevelUnlocked;
    }

    public void setMaxLevelUnlocked(Integer maxLevelUnlocked) {
        this.maxLevelUnlocked = maxLevelUnlocked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
