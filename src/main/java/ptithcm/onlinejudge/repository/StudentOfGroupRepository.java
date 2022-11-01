package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.StudentOfGroup;
import ptithcm.onlinejudge.model.entity.StudentOfGroupId;

import java.util.List;
import java.util.Optional;

public interface StudentOfGroupRepository extends JpaRepository<StudentOfGroup, StudentOfGroupId> {
    @Query(value = "select * from student_of_group where student_id = ?1", nativeQuery = true)
    List<StudentOfGroup> getStudentOfGroupsByStudentId(String studentId);

    @Query(value = "select * from student_of_group where subject_class_group_id=?1", nativeQuery = true)
    List<StudentOfGroup> getStudentOfGroupsByGroupId(String groupId);

    @Query(value = "select * " +
            "from student_of_group " +
            "where student_of_group.student_id = ?1 " +
            "and student_of_group.subject_class_group_id in (select subject_class_groups.subject_class_group_id " +
            "from subject_class_groups " +
            "where subject_class_groups.subject_class_id = ?2)", nativeQuery = true)
    Optional<StudentOfGroup> findByStudentIdAndSubjectClassId(String studentId, String subjectClassId);

    @Query(value = "select * from student_of_group where student_of_group.student_id = ?1 and student_of_group.subject_class_group_id = ?2", nativeQuery = true)
    Optional<StudentOfGroup> findByStudentIdAndSubjectClassGroupId(String studentId, String subjectClassGroupId);
}