package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}