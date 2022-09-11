package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Byte> {
}