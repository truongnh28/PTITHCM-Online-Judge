package ptithcm.onlinejudge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

import java.util.List;

public interface SubjectClassGroupRepository extends JpaRepository<SubjectClassGroup, String> {
    @Query(value = "select * from subject_class_groups where subject_class_id = ?1",
            countQuery = "select count(*) from subject_class_groups where subject_class_id = ?1",
            nativeQuery = true)
    Page<SubjectClassGroup> getGroupsOfClass(String subjectClassId, Pageable pageable);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and (lower(subject_class_group_id) like lower(?2) or lower(subject_class_group_name) like lower(?2))",
            countQuery = "select count(*) from subject_class_groups where subject_class_id = ?1 and (lower(subject_class_group_id) like lower(?2) or lower(subject_class_group_name) like lower(?2))",
            nativeQuery = true)
    Page<SubjectClassGroup> searchGroupsOfClassByKeyword(String classId, String keyword, Pageable pageable);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and hide = 0",
            countQuery = "select count(*) from subject_class_groups where subject_class_id = ?1 and hide = 0",
            nativeQuery = true)
    Page<SubjectClassGroup> getGroupsOfClassActive(String classId, Pageable pageable);

    @Query(value = "select * from subject_class_groups where subject_class_id = ?1 and hide = 0 and (lower(subject_class_group_id) like lower(?2) or lower(subject_class_group_name) like lower(?2))",
            countQuery = "select count(*) from subject_class_groups where subject_class_id = ?1 and hide = 0 and (lower(subject_class_group_id) like lower(?2) or lower(subject_class_group_name) like lower(?2))",
            nativeQuery = true)
    Page<SubjectClassGroup> searchGroupsOfClassByKeywordActive(String classId, String keyword, Pageable pageable);

    @Query(value = "select * from subject_class_groups where hide = 0 and subject_class_groups.subject_class_group_id in (select student_of_group.subject_class_group_id from student_of_group where student_of_group.student_id = ?1)",
            countQuery = "select count(*) from subject_class_groups where hide = 0 and subject_class_groups.subject_class_group_id in (select student_of_group.subject_class_group_id from student_of_group where student_of_group.student_id = ?1)",
            nativeQuery = true)
    Page<SubjectClassGroup> getGroupsThatHasStudent(String studentId, Pageable pageable);

    @Query(value = "select * from subject_class_groups where hide = 0 and (lower(subject_class_groups.subject_class_group_id) like lower(?2) or lower(subject_class_groups.subject_class_group_name) like lower(?2)) and subject_class_groups.subject_class_group_id in (select student_of_group.subject_class_group_id from student_of_group where student_of_group.student_id = ?1)",
            countQuery = "select count(*) from subject_class_groups where hide = 0 and (lower(subject_class_groups.subject_class_group_id) like lower(?2) or lower(subject_class_groups.subject_class_group_name) like lower(?2)) and subject_class_groups.subject_class_group_id in (select student_of_group.subject_class_group_id from student_of_group where student_of_group.student_id = ?1)",
            nativeQuery = true)
    Page<SubjectClassGroup> searchGroupsThatHasStudent(String studentId, String keyword, Pageable pageable);
}