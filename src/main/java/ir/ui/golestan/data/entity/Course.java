package ir.ui.golestan.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
