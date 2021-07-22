package ua.casten.bowling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.casten.bowling.model.ViewFrame;
import ua.casten.bowling.service.BowlingService;

import java.util.Arrays;

@Controller
@RequestMapping("/bowling")
public class BowlingController {

    private final BowlingService bowlingService;

    @Autowired
    public BowlingController(BowlingService bowlingService) {
        this.bowlingService = bowlingService;
    }

    @GetMapping("")
    public String startNewGame() {
        return "new-game";
    }

    @GetMapping("/{gameId}")
    public String getBowlingPage(@PathVariable int gameId,
                                 Model model,
                                 String errorMessage) {
        if (!bowlingService.isStarted()) {
            return "redirect:/bowling";
        }
        bowlingService.setGameId(gameId);
        addAttributesToModel(model);
        model.addAttribute("errorMessage", errorMessage);
        return "bowling-scoreboard";
    }

    @PostMapping("/{gameId}")
    public String confirmScore(@PathVariable int gameId,
                               @RequestParam("score") String score,
                               Model model) {
        String errorMessage = bowlingService.makePoll(score);

        if (!errorMessage.isEmpty()) {
            return getBowlingPage(gameId, model, errorMessage);
        }

        return "redirect:/bowling/" + gameId;
    }

    @PostMapping("/new")
    public String restartGame() {
        int gameId = bowlingService.startNewGame();
        return "redirect:/bowling/" + gameId;
    }

    private void addAttributesToModel(Model model) {
        ViewFrame[] viewFrames = bowlingService.getFrames();
        model.addAttribute("regularFrames", Arrays.copyOfRange(viewFrames, 0, 9));
        model.addAttribute("lastFrame", viewFrames[9]);
        model.addAttribute("isFinished", bowlingService.isFinished());
    }

}
