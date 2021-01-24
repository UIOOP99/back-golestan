package ir.ui.golestan.data.entity;

import lombok.*;

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
public final class Score {


	@Id
    long studentId;

    @Id
    int courseId;

    double score;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class ScoreId implements Serializable {

        long studentId;

        int courseId;
    }
}
