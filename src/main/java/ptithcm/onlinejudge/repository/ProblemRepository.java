package ptithcm.onlinejudge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Problem;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, String> {
    @Query(value = "select * from problems where teacher_id = ?1", nativeQuery = true)
    List<Problem> getProblemsByTeacher(String teacherId);

    @Query(value = "select * from problems where problems.problem_id in (select contest_has_problem.problem_id from contest_has_problem where contest_id = ?1)", nativeQuery = true)
    List<Problem> getProblemsByContestId(String contestId);
}
