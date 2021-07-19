package ua.casten.bowling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String getBowlingPage(Model model, String errorMessage) {
        addAttributesToModel(model);
        model.addAttribute("errorMessage", errorMessage);
        return "bowling-scoreboard";
    }

    @PostMapping()
    public String confirmScore(@RequestParam("score") String score,
                               Model model) {
        String errorMessage = bowlingService.makePoll(score);

        if (!errorMessage.isEmpty()) {
            return getBowlingPage(model, errorMessage);
        }

        return "redirect:/bowling";
    }

    @PostMapping("/restart")
    public String restartGame() {
        bowlingService.startNewGame();
        return "redirect:/bowling";
    }

    private void addAttributesToModel(Model model) {
        ViewFrame[] viewFrames = bowlingService.getFrames();
        model.addAttribute("regularFrames", Arrays.copyOfRange(viewFrames, 0, 9));
        model.addAttribute("lastFrame", viewFrames[9]);
        model.addAttribute("isFinished", bowlingService.isFinished());
    }

}
