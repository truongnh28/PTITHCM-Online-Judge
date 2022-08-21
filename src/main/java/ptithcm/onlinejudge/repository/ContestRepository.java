package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Contest;

public interface ContestRepository extends JpaRepository<Contest, String> {
}