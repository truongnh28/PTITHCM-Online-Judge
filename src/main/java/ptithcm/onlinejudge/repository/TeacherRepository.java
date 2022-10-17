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

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)", nativeQuery = true)
    List<Teacher> getTeachersOwnClass(String classId);

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id like %?2% and teachers.teacher_id in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)", nativeQuery = true)
    List<Teacher> searchTeachersOwnClassById(String classId, String keyword);

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id not in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)", nativeQuery = true)
    List<Teacher> getTeachersNotOwnClass(String classId);

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id like %?2% and teachers.teacher_id not in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)", nativeQuery = true)
    List<Teacher> searchTeachersNotOwnClass(String classId, String keyword);
}