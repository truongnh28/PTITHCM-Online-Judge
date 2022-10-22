package ptithcm.onlinejudge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClass;

import java.util.List;

public interface SubjectClassRepository extends JpaRepository<SubjectClass, String> {
    @Query(value = "select * from subject_classes where subject_classes.subject_id = ?1",
            countQuery = "select count(*) from subject_classes where subject_classes.subject_id = ?1",
            nativeQuery = true)
    Page<SubjectClass> getSubjectClassOfSubject(String subjectId, Pageable pageable);

    @Query(value = "select * from subject_classes where subject_classes.subject_id = ?1 and (subject_classes.subject_class_id like %?2% or subject_classes.subject_class_name like %?2%)",
            countQuery = "select count(*) from subject_classes where subject_classes.subject_id = ?1 and (subject_classes.subject_class_id like %?2% or subject_classes.subject_class_name like %?2%)",
            nativeQuery = true)
    Page<SubjectClass> searchSubjectClassOfSubjectByKeyword(String subjectId, String keyword, Pageable pageable);

    @Query(value = "select * from subject_classes where subject_classes.hide = 0 and subject_classes.subject_class_id in (select subject_class_has_teacher.subject_class_id from subject_class_has_teacher where subject_class_has_teacher.teacher_id = ?1)", nativeQuery = true)
    List<SubjectClass> getClassesTeacherOwnActive(String teacherId);

    @Query(value = "select * from subject_classes where subject_classes.hide = 0 and (subject_classes.subject_class_id like %?2% or subject_classes.subject_class_name like %?2%) and subject_classes.subject_class_id in (select subject_class_has_teacher.subject_class_id from subject_class_has_teacher where subject_class_has_teacher.teacher_id = ?1)", nativeQuery = true)
    List<SubjectClass> searchClassesTeacherOwnActive(String teacherId, String keyword);
}