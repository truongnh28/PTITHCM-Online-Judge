package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

public interface SubjectClassGroupRepository extends JpaRepository<SubjectClassGroup, String> {
}