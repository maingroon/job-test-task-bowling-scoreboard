package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.exception.BowlingRuntimeException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.repository.GameRepository;
import ua.casten.bowling.util.BowlingUtil;

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
    public void makeRoll(Game game, String stringScore) throws BowlingException {
        if (game.isFinished()) {
            throw new BowlingRuntimeException("A new roll cannot be made, the game is over.");
        }

        var score = BowlingUtil.validateScore(stringScore);

        if (!regularFrameService.makeRoll(game, score)) {
            lastFrameService.makeRoll(game, score);
        }

        updateGameData(game);
    }

    private void updateGameData(Game game) {

        if (game.getLastFrame() != null) {
            var lastFrame = game.getLastFrame();
            game.setFullScore(lastFrame.getScore());
        } else {
            var regularFrames = BowlingUtil.sortFrames(game.getRegularFrames());
            game.setFullScore(regularFrames.get(regularFrames.size() - 1).getScore());
        }

        gameRepository.save(game);
    }

}
