package ua.casten.bowling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.repository.GameRepository;
import ua.casten.bowling.service.BowlingService;
import ua.casten.bowling.util.BowlingUtil;

import javax.validation.constraints.Max;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Min;

@Controller
@RequestMapping("/bowling")
public class BowlingController {

    private final BowlingService bowlingService;
    private final GameRepository gameRepository;

    @Autowired
    public BowlingController(BowlingService bowlingService, GameRepository gameRepository) {
        this.bowlingService = bowlingService;
        this.gameRepository = gameRepository;
    }

    @GetMapping("")
    public String startNewGame() {
        return "new-game";
    }

    @GetMapping("/{gameId}")
    public String getBowlingPage(@PathVariable long gameId,
                                 Model model,
                                 String exceptionMessage) {
        var game = gameRepository.getById(gameId);
        model.addAttribute("exceptionMessage", exceptionMessage);
        addAttributesToModel(model, game);
        return "bowling-scoreboard";
    }

    @PostMapping("/{gameId}")
    public String confirmScore(@PathVariable long gameId,
                               @RequestParam("score")
                               @Min(value = 0, message = "Score cannot be less than 0.")
                               @Max(value = 10, message = "Score cannot be greater than 10.")
                                       int score)
                               throws BowlingException {
        var game = gameRepository.getById(gameId);
        bowlingService.makeRoll(game, score);
        return "redirect:/bowling/" + gameId;
    }

    @PostMapping("/new")
    public String restartGame() {
        var gameId = bowlingService.startNewGame();
        return "redirect:/bowling/" + gameId;
    }

    @ExceptionHandler({BowlingException.class, BowlingRuntimeException.class, EntityNotFoundException.class})
    public String errorPage(Model model, Exception exception) {
        model.addAttribute("exceptionMessage", exception.getMessage());
        return "error";
    }

    private void addAttributesToModel(Model model, Game game) {
        var regularFrames = BowlingUtil.parseRegularFrames(game);
        var lastFrame = BowlingUtil.parseLastFrame(game);
        model.addAttribute("regularFrames", regularFrames);
        model.addAttribute("lastFrame", lastFrame);
        model.addAttribute("fullScore", game.getFullScore());
        model.addAttribute("isFinished", game.isFinished());
    }

}
