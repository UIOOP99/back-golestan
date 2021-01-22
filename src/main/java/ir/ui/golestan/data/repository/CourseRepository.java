package ir.ui.golestan.data.repository;

import ir.ui.golestan.data.entity.Course;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findAllByProfessorId(int professorId);

    List<Course> findAllByProfessorIdAndSemesterId(int professorId, int semesterId);

    List<Course> findAllBySemesterId(int semesterId);

    List<Course> findAllByStudentId(int studentId);
}

