package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClass;

import java.util.List;

public interface SubjectClassRepository extends JpaRepository<SubjectClass, String> {
    @Query(value = "select * from subject_classes where subject_classes.subject_id = ?1", nativeQuery = true)
    List<SubjectClass> getSubjectClassOfSubject(String subjectId);

    @Query(value = "select * from subject_classes where subject_classes.subject_id = ?1 and (subject_classes.subject_class_id like %?2% or subject_classes.subject_class_name like %?2%)", nativeQuery = true)
    List<SubjectClass> searchSubjectClassOfSubjectByIdOrName(String subjectId, String keyword);
}