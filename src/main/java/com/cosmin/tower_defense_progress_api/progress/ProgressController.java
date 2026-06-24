package com.cosmin.tower_defense_progress_api.progress;


import com.cosmin.tower_defense_progress_api.dto.PlayerProgressResponse;
import com.cosmin.tower_defense_progress_api.dto.UpdateLevelRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/progress")
    public PlayerProgressResponse getProgress(){
        return progressService.getProgress();
    }

    @PutMapping("/levels/{levelNumber}")
    public PlayerProgressResponse updateLevel(@PathVariable Integer levelNumber
            , @Valid @RequestBody UpdateLevelRequest request){
        return progressService.updateLevel(levelNumber,request);
    }

    @PostMapping("/progress/reset")
    public PlayerProgressResponse resetProgress() {
        return progressService.resetProgress();
    }

}
