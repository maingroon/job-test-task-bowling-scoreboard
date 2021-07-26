package ua.casten.bowling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.casten.bowling.model.RegularFrame;

@Repository
public interface RegularFrameRepository extends JpaRepository<RegularFrame, Long> {
}
