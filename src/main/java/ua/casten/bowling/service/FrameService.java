package ua.casten.bowling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.casten.bowling.model.Frame;
import ua.casten.bowling.model.repository.FrameRepository;

import java.util.List;
import java.util.Objects;

@Service
public class FrameService {

    private final FrameRepository frameRepository;

    @Autowired
    public FrameService(FrameRepository frameRepository) {
        this.frameRepository = frameRepository;
    }

    public Frame save(Frame frame) {
        Objects.requireNonNull(frame);

        return frameRepository.save(frame);
    }

    public List<Frame> getByGameId(int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("gameId cannot be less than 0.");
        }

        return frameRepository.findFramesByGameId(gameId);
    }

}
