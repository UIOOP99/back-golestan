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

    private int semesterId;

    private int professorId;

    @OrderColumn
    private int[] studentsIds;

}
