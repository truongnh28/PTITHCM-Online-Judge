package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query(value = "select * from teachers where teachers.role_id != 1", nativeQuery = true)
    List<Teacher> getAllTeacherExceptAdmin();

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id like %?1%", nativeQuery = true)
    List<Teacher> searchTeachersByKeywordExceptAdmin(String keyword);
}