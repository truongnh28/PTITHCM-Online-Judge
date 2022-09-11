package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Contest;

@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
}