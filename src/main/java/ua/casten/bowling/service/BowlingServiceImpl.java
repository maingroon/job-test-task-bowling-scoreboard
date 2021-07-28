package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.repository.GameRepository;

@Service
public class BowlingServiceImpl implements BowlingService {

    private final GameRepository gameRepository;
    private final RegularFrameService regularFrameService;
    private final LastFrameService lastFrameService;

    @Autowired
    public BowlingServiceImpl(GameRepository gameRepository,
                              RegularFrameService regularFrameService,
                              LastFrameService lastFrameService) {
        this.gameRepository = gameRepository;
        this.regularFrameService = regularFrameService;
        this.lastFrameService = lastFrameService;
    }

    @Override
    public long startNewGame() {
        return gameRepository.save(new Game()).getId();
    }

    @Override
    public void makeRoll(Game game, int score) throws BowlingException {
        if (game.isFinished()) {
            throw new BowlingRuntimeException("A new roll cannot be made, the game is over.");
        }

        getFrameService(game).makeRoll(game, score);
        updateGameData(game);
    }

    private FrameService getFrameService(Game game) {
        var lastPlayedFrame = game.getLastPlayedFrame();
        if (lastFrameInGame(lastPlayedFrame)) {
            return lastFrameService;
        }
        return regularFrameService;
    }

    private boolean lastFrameInGame(Frame lastPlayedFrame) {
        return lastPlayedFrame != null
                && (lastPlayedFrame.getNumber() == 10
                        || (lastPlayedFrame.getNumber() == 9
                        && lastPlayedFrame.isPlayed()));
    }

    private void updateGameData(Game game) {
        regularFrameService.updateFramesData(game);
        if (game.getLastFrame() != null) {
            lastFrameService.updateFramesData(game);
        }

        var lastPlayedFrame = game.getLastPlayedFrame();
        if (lastPlayedFrame != null) {
            game.setFullScore(lastPlayedFrame.getScore());
        }
        gameRepository.save(game);
    }

}
