package ir.ui.golestan.data.repository;

import ir.ui.golestan.data.entity.UserRole;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    UserRole findRole(int userId);

    List<Integer> findAllProfessorsList();

    List<Integer> findAllStudentsList();
}
