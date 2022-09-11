package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.ProblemType;

public interface ProblemTypeRepository extends JpaRepository<ProblemType, String> {
}