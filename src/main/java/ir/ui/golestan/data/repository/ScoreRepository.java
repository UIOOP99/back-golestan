package ir.ui.golestan.data.repository;

import ir.ui.golestan.data.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Score.ScoreId> {
}
