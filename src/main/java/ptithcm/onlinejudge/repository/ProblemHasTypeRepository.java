package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.ProblemHasType;
import ptithcm.onlinejudge.model.entity.ProblemHasTypeId;

public interface ProblemHasTypeRepository extends JpaRepository<ProblemHasType, ProblemHasTypeId> {
}