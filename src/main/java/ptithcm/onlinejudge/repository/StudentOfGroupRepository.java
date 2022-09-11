package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.StudentOfGroup;
import ptithcm.onlinejudge.model.entity.StudentOfGroupId;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

import java.util.List;

public interface StudentOfGroupRepository extends JpaRepository<StudentOfGroup, StudentOfGroupId> {
    @Query(value = "select * from student_of_group where student_id = ?1", nativeQuery = true)
    List<StudentOfGroup> getStudentOfGroupsByStudentId(String studentId);
}