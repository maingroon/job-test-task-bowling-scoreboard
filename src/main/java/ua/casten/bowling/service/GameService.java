package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.exception.BowlingException;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.repository.GameRepository;

import java.util.Objects;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final FrameService frameService;

    @Autowired
    public GameService(GameRepository gameRepository, FrameService frameService) {
        this.gameRepository = gameRepository;
        this.frameService = frameService;
    }

    public Game save(Game game) {
        Objects.requireNonNull(game);

        return gameRepository.save(game);
    }

    public Game findById(int gameId) throws BowlingException {
        if (gameId < 0) {
            throw new IllegalArgumentException("gameId cannot be less than 0.");
        }

        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new BowlingException(String.format("Game with id %d not found", gameId)));
        game.setFrames(frameService.getByGameId(gameId));

        return game;
    }

}
