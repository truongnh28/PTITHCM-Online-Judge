package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.SubjectClass;

public interface SubjectClassRepository extends JpaRepository<SubjectClass, String> {
}