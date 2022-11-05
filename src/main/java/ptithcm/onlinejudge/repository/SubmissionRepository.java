package ptithcm.onlinejudge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Submission;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    @Query(value = "select * from submissions where submissions.contest_id = ?1 order by submissions.submission_time desc",
            countQuery = "select count(*) from submissions where submissions.contest_id = ?1 order by submissions.submission_time desc",
            nativeQuery = true)
    Page<Submission> getSubmissionsOfContest(String contestId, Pageable pageable);

    @Query(value = "select * from submissions where submissions.contest_id = ?1 and lower(submissions.student_id) like lower(?2) order by submissions.submission_time desc",
            countQuery = "select count(*) from submissions where submissions.contest_id = ?1 and lower(submissions.student_id) like lower(?2) order by submissions.submission_time desc",
            nativeQuery = true)
    Page<Submission> searchSubmissionOfContestByKeyword(String contestId, String keyword, Pageable pageable);

    @Query(value = "select count(*) from submissions where submissions.contest_id = ?1", nativeQuery = true)
    Long countAllSubmissionsByContestId(String contestId);

    @Query(value = "select count(*) from submissions where submissions.verdict = 1 and submissions.contest_id = ?1", nativeQuery = true)
    Long countAcceptedSubmissionsByContestId(String contestId);

    @Query(value = "select count(*) from (select submissions.student_id from submissions where submissions.contest_id = ?1 group by submissions.student_id) as temp", nativeQuery = true)
    Long countAllStudentsSubmittedByContestId(String contestId);

    @Query(value = "select count(*) from (select submissions.student_id from submissions where submissions.contest_id = ?1 and submissions.verdict = 1 group by submissions.student_id) as temp", nativeQuery = true)
    Long countAllStudentsAcceptedByContestId(String contestId);

    @Query(value = "select count(*) from submissions where submissions.contest_id = ?1 and submissions.problem_id = ?2", nativeQuery = true)
    Long countSubmissionsByContestIdAndProblemId(String contestId, String problemId);

    @Query(value = "select count(*) from submissions where submissions.verdict = 1 and submissions.contest_id = ?1 and submissions.problem_id = ?2", nativeQuery = true)
    Long countAcceptedSubmissionByContestIdAndProblemId(String contestId, String problemId);

    @Query(value = "select count(*) from (select submissions.student_id from submissions where submissions.contest_id = ?1 and submissions.problem_id = ?2 group by submissions.student_id) as temp", nativeQuery = true)
    Long countStudentsSubmittedByContestIdAndProblemId(String contestId, String problemId);

    @Query(value = "select count(*) from (select submissions.student_id from submissions where submissions.contest_id = ?1 and submissions.problem_id = ?2 and submissions.verdict = 1 group by submissions.student_id) as temp", nativeQuery = true)
    Long countStudentAcceptedByContestIdAndProblemId(String contestId, String problemId);

    @Query(value = "select * from submissions where submissions.contest_id = ?1 and submissions.problem_id = ?2 and submissions.student_id = ?3 and submissions.verdict = 1", nativeQuery = true)
    List<Submission> problemSolvedByStudent(String contestId, String problemId, String studentId);
}