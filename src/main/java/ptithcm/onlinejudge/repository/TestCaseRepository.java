package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.TestCase;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, String> {
    @Query(value = "select * from test_case where test_case.problem_id = ?1 order by length(test_case.test_case_in), test_case.test_case_in asc", nativeQuery = true)
    List<TestCase> getTestCasesByProblem(String problemId);

    @Query(value = "select count(*) from test_case where test_case.problem_id = ?1", nativeQuery = true)
    int countTestCaseByProblem(String problemId);
}