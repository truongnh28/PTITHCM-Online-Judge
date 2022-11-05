package ptithcm.onlinejudge.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Problem;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, String> {
    @Query(value = "select * from problems where teacher_id = ?1",
            countQuery = "select count(*) from problems where teacher_id = ?1",
            nativeQuery = true)
    Page<Problem> getAllProblemsByTeacher(String teacherId, Pageable pageable);

    @Query(value = "select * from problems where teacher_id = ?1 and (lower(problems.problem_id) like lower(?2) or lower(problems.problem_name) like lower(?2))",
            countQuery = "select count(*) from problems where teacher_id = ?1 and (lower(problems.problem_id) like lower(?2) or lower(problems.problem_name) like lower(?2))",
            nativeQuery = true)
    Page<Problem> searchAllProblemsByTeacher(String teacherId, String keyword, Pageable pageable);

    @Query(value = "select * from problems where problems.hide = 0", nativeQuery = true)
    List<Problem> getProblemsActive();

    @Query(value = "select * from problems where problems.problem_id in (select contest_has_problem.problem_id from contest_has_problem where contest_id = ?1)", nativeQuery = true)
    List<Problem> getProblemsByContestId(String contestId);

    @Query(value = "select * from problems where problems.hide = 0 and problems.problem_id in (select contest_has_problem.problem_id from contest_has_problem where contest_id = ?1)", nativeQuery = true)
    List<Problem> getProblemsActiveOfContest(String contestId);

    @Query(value = "select * from problems where problems.hide = 0 and problems.problem_id not in (select contest_has_problem.problem_id from contest_has_problem where contest_has_problem.contest_id = ?1)",
            countQuery = "select count(*) from problems where problems.hide = 0 and problems.problem_id not in (select contest_has_problem.problem_id from contest_has_problem where contest_has_problem.contest_id = ?1)",
            nativeQuery = true)
    Page<Problem> getProblemsActiveNotInContest(String contestId, Pageable pageable);

    @Query(value = "select * from problems where problems.hide = 0 and (lower(problems.problem_id) like lower(?2) or lower(problems.problem_name) like lower(?2)) and problems.problem_id not in (select contest_has_problem.problem_id from contest_has_problem where contest_has_problem.contest_id = ?1)",
            countQuery = "select count(*) from problems where problems.hide = 0 and (lower(problems.problem_id) like lower(?2) or lower(problems.problem_name) like lower(?2)) and problems.problem_id not in (select contest_has_problem.problem_id from contest_has_problem where contest_has_problem.contest_id = ?1)",
            nativeQuery = true)
    Page<Problem> searchProblemsActiveNotInContest(String contestId, String keyword, Pageable pageable);
}
