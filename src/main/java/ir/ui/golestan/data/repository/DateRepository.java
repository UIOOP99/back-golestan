package ir.ui.golestan.data.repository;

import ir.ui.golestan.data.entity.CourseDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends JpaRepository<CourseDate, Integer> {
}

