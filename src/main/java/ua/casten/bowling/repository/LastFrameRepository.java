package ua.casten.bowling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.casten.bowling.model.LastFrame;

@Repository
public interface LastFrameRepository extends JpaRepository<LastFrame, Long> {
}
