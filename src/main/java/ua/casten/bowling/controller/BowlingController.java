package ua.casten.bowling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.repository.GameRepository;
import ua.casten.bowling.service.BowlingService;
import ua.casten.bowling.util.BowlingUtil;
import ua.casten.bowling.util.LastFrameDtoBuilder;
import ua.casten.bowling.util.RegularFrameDtoBuilder;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
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
                               @RequestParam("score") @Min(0) @Max(10) int score)
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

    @ExceptionHandler({BowlingException.class, BowlingRuntimeException.class})
    public String bowlingExceptionHandler(Model model, Exception exception) {
        return errorPage(model, exception.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public String numberFormatExceptionHandler(Model model) {
        return errorPage(model, "Enter valid score (without symbols, spaces or empty field).");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String constraintViolationExceptionHandler(Model model) {
        return errorPage(model, "Score cannot be less than 0 or greater than 10.");
    }

    private String errorPage(Model model, String message) {
        model.addAttribute("exceptionMessage", message);
        return "error";
    }

    private void addAttributesToModel(Model model, Game game) {
        var regularFrames =
                RegularFrameDtoBuilder.transferRegularFrames(BowlingUtil.sortFrames(game.getRegularFrames()));
        var lastFrame = LastFrameDtoBuilder.transferLastFrame(game.getLastFrame());
        model.addAttribute("regularFrames", regularFrames);
        model.addAttribute("lastFrame", lastFrame);
        model.addAttribute("fullScore", game.getFullScore());
        model.addAttribute("isFinished", game.isFinished());
    }

}
