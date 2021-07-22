package ua.casten.bowling.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.casten.bowling.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
