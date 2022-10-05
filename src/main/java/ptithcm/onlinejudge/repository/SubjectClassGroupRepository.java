package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

import java.util.List;

public interface SubjectClassGroupRepository extends JpaRepository<SubjectClassGroup, String> {
    @Query(value = "select * from subject_class_groups where subject_class_id = ?1", nativeQuery = true)
    List<SubjectClassGroup> getSubjectClassGroupsBySubjectClass(String subjectClassId);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and (subject_class_group like %?2% or subject_class_name like %?2%)", nativeQuery = true)
    List<SubjectClassGroup> searchGroupByIdOrName(String classId, String keyword);
}