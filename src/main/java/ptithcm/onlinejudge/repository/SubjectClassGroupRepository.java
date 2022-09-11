package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

import java.util.List;

public interface SubjectClassGroupRepository extends JpaRepository<SubjectClassGroup, String> {
    @Query(value = "select * from subject_class_groups where subject_class_id = ?1", nativeQuery = true)
    List<SubjectClassGroup> getSubjectClassGroupsBySubjectClass(String subjectClassId);
}