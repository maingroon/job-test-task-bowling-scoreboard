package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.model.Game;
import ua.casten.bowling.model.repository.GameRepository;

import java.util.Objects;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game save(Game game) {
        Objects.requireNonNull(game);

        return gameRepository.save(game);
    }

    public Game getById(Long gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("gameId cannot be less than 0.");
        }

        return gameRepository.getById(gameId);
    }

}
