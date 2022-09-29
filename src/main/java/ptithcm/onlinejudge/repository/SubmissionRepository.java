package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
}