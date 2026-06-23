package com.cosmin.tower_defense_progress_api.levelProgress;

import com.cosmin.tower_defense_progress_api.user.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "level_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "level_number"})
        }
)
public class LevelProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer levelNumber;

    private Integer starUnlocked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public LevelProgress(Long id, Integer levelNumber, Integer starUnlocked, User user) {
        this.id = id;
        this.levelNumber = levelNumber;
        this.starUnlocked = starUnlocked;
        this.user = user;
    }
    public LevelProgress() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Integer levelNumber) {
        this.levelNumber = levelNumber;
    }

    public Integer getStarUnlocked() {
        return starUnlocked;
    }

    public void setStarUnlocked(Integer starUnlocked) {
        this.starUnlocked = starUnlocked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
