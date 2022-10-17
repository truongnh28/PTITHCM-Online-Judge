package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

import java.util.List;

public interface SubjectClassGroupRepository extends JpaRepository<SubjectClassGroup, String> {
    @Query(value = "select * from subject_class_groups where subject_class_id = ?1", nativeQuery = true)
    List<SubjectClassGroup> getSubjectClassGroupsBySubjectClass(String subjectClassId);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and (subject_class_group_id like %?2% or subject_class_group_name like %?2%)", nativeQuery = true)
    List<SubjectClassGroup> searchGroupByIdOrName(String classId, String keyword);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and hide = 0", nativeQuery = true)
    List<SubjectClassGroup> getGroupsOfClassActive(String classId);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and hide = 0 and (subject_class_group_id like %?2% or subject_class_group_name like %?2%)", nativeQuery = true)
    List<SubjectClassGroup> searchGroupsOfClassActiveByIdOrName(String classId, String keyword);

    @Query(value = "select * from subject_class_groups where hide = 0 and subject_class_groups.subject_class_group_id in (select student_of_group.subject_class_group_id from student_of_group where student_of_group.student_id = ?1)", nativeQuery = true)
    List<SubjectClassGroup> getGroupsThatHasStudent(String studentId);

    @Query(value = "select * from subject_class_groups where hide = 0 and (subject_class_groups.subject_class_group_id = ?2 or subject_class_groups.subject_class_group_name = ?2) and subject_class_groups.subject_class_group_id in (select student_of_group.subject_class_group_id from student_of_group where student_of_group.student_id = ?1)", nativeQuery = true)
    List<SubjectClassGroup> searchGroupsThatHasStudent(String studentId, String keyword);
}