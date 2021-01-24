package ir.ui.golestan.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy.Reiterating;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int units;

    private int semesterId;

    @OneToMany
    private List<CourseDate> dates;

    private long professorId;

    @OrderColumn
    private long[] studentsIds;

}
