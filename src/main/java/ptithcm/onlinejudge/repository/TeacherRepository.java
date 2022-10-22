package ptithcm.onlinejudge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Query(value = "select * from teachers where teachers.role_id != 1",
            countQuery = "select count(*) from teachers where teachers.role_id != 1",
            nativeQuery = true)
    Page<Teacher> getAllTeacherExceptAdmin(Pageable pageable);

    @Query(value = "select * from teachers where teachers.role_id != 1 and lower(teachers.teacher_id) like lower(?1)",
            countQuery = "select count(*) from teachers where teachers.role_id != 1 and teachers.teacher_id like lower(?1)",
            nativeQuery = true)
    Page<Teacher> searchTeachersByKeywordExceptAdmin(String keyword, Pageable pageable);

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)", nativeQuery = true)
    List<Teacher> getTeachersOwnClass(String classId);

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id like %?2% and teachers.teacher_id in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)", nativeQuery = true)
    List<Teacher> searchTeachersOwnClassById(String classId, String keyword);

    @Query(value = "select * from teachers where teachers.role_id != 1 and teachers.teacher_id not in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)",
            countQuery = "select count(*) from teachers where teachers.role_id != 1 and teachers.teacher_id not in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)",
            nativeQuery = true)
    Page<Teacher> getTeachersNotOwnClass(String classId, Pageable pageable);

    @Query(value = "select * from teachers where teachers.role_id != 1 and lower(teachers.teacher_id) like lower(?2) and teachers.teacher_id not in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)",
            countQuery = "select count(*) from teachers where teachers.role_id != 1 and lower(teachers.teacher_id) like lower(?2) and teachers.teacher_id not in (select subject_class_has_teacher.teacher_id from subject_class_has_teacher where subject_class_has_teacher.subject_class_id = ?1)",
            nativeQuery = true)
    Page<Teacher> searchTeachersNotOwnClassByKeyword(String classId, String keyword, Pageable pageable);
}