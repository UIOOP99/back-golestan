package ir.ui.golestan.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy.Reiterating;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    private int id;

    private int professorId;

    private String studentsIds;

    protected Course() {}

    public Course(int id, int professorId, String studentsIds){
        this.id = id;
        this.professorId = professorId;
        this.studentsIds = studentsIds;
    }

    @Override
    public String toString() {
        return String.format("Course: %d\nProfessorID: %d\nStudentIDs: %s\n**********",
        id, professorId, studentsIds);
    }

    public void setStudentsIds(List<Integer> studentsIds) {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = studentsIds.iterator();

        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) sb.append(", ");
        }

        this.studentsIds = sb.toString();
    }

    public List<Integer> getStudentsIds() {
        return Stream.of(studentsIds.split(", "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
