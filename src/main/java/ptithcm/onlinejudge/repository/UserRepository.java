package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}