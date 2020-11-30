package ir.ui.golestan.data.repository;

import ir.ui.golestan.data.entity.Score;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Score.ScoreId> {
    @Query("SELECT s FROM Score s WHERE s.id = :id")
    Collection<Score> findScoresById(@Param("id") int id);
}

