package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.StudentOfGroup;
import ptithcm.onlinejudge.model.entity.StudentOfGroupId;

public interface StudentOfGroupRepository extends JpaRepository<StudentOfGroup, StudentOfGroupId> {
}