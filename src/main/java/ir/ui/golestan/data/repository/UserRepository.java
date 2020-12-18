package ir.ui.golestan.data.repository;

import ir.ui.golestan.authorization.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AuthenticatedUser, Integer> {
}
