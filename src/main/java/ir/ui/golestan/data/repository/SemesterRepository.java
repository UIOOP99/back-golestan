package ir.ui.golestan.data.repository;

import ir.ui.golestan.data.entity.Semester;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    List<Semester> findAllByStudentId(int studentId);
}
