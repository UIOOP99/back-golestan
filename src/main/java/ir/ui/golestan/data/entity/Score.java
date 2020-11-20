package ir.ui.golestan.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Score.ScoreId.class)
public class Score {

    @Id
    int studentId;

    @Id
    int courseId;

    double score;

    @AllArgsConstructor(staticName = "of")
    public static class ScoreId implements Serializable {

        int studentId;

        int courseId;
    }
}
