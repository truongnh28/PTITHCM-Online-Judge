package ptithcm.onlinejudge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Problem;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, String> {
    @Query(value = "select * from problems where teacher_id = ?1", nativeQuery = true)
    List<Problem> getAllProblemsByTeacher(String teacherId);

    @Query(value = "select * from problems where teacher_id = ?1 and (problems.problem_id like %?2% or problems.problem_name like %?2%)", nativeQuery = true)
    List<Problem> searchAllProblemsByTeacher(String teacherId, String keyword);

    @Query(value = "select * from problems where problems.hide = 0", nativeQuery = true)
    List<Problem> getProblemsActive();

    @Query(value = "select * from problems where problems.problem_id in (select contest_has_problem.problem_id from contest_has_problem where contest_id = ?1)", nativeQuery = true)
    List<Problem> getProblemsByContestId(String contestId);

    @Query(value = "select * from problems where problems.hide = 0 and problems.problem_id in (select contest_has_problem.problem_id from contest_has_problem where contest_id = ?1)", nativeQuery = true)
    List<Problem> getProblemsActiveOfContest(String contestId);

    @Query(value = "select * from problems where problems.hide = 0 and problems.problem_id not in (select contest_has_problem.problem_id from contest_has_problem where contest_has_problem.contest_id = ?1)", nativeQuery = true)
    List<Problem> getProblemsActiveNotInContest(String contestId);

    @Query(value = "select * from problems where problems.hide = 0 and (problems.problem_id like %?2% or problems.problem_name like %?2%) and problems.problem_id not in (select contest_has_problem.problem_id from contest_has_problem where contest_has_problem.contest_id = ?1)", nativeQuery = true)
    List<Problem> searchProblemsActiveNotInContest(String contestId, String keyword);
}
