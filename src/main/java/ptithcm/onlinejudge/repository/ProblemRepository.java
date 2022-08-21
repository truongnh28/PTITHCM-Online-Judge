package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, String> {
}