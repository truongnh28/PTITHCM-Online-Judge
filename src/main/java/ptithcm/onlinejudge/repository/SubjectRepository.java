package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String> {
}