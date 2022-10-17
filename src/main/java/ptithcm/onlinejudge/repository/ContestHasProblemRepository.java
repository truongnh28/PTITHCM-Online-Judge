package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.ContestHasProblem;
import ptithcm.onlinejudge.model.entity.ContestHasProblemId;

import java.util.List;

public interface ContestHasProblemRepository extends JpaRepository<ContestHasProblem, ContestHasProblemId> {
}