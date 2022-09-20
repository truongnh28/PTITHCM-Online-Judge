package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Byte> {
}
