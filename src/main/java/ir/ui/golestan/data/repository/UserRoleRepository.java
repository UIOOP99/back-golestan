package ir.ui.golestan.data.repository;

import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.UserRole;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findAllByRole(Role role);

}
