package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.ProblemHasType;
import ptithcm.onlinejudge.model.entity.ProblemHasTypeId;

import javax.transaction.Transactional;

@Repository
public interface ProblemHasTypeRepository extends JpaRepository<ProblemHasType, ProblemHasTypeId> {
    @Transactional
    @Modifying
    @Query(value = "delete from problem_has_type where problem_has_type.problem_id = ?1", nativeQuery = true)
    void deleteByProblem(String problemId);
}