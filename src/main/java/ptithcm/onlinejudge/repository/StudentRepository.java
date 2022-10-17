package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> searchStudentByIdLikeIgnoreCase(String studentId);

    @Query(value = "select * " +
            "from students " +
            "where students.student_id in (" +
            "select student_of_group.student_id " +
            "from student_of_group " +
            "where student_of_group.subject_class_group_id = ?1)",nativeQuery = true)
    List<Student> getStudentsOfGroup(String groupId);

    @Query(value = "select * " +
            "from students " +
            "where students.student_id like %?2% and students.student_id in (" +
            "select student_of_group.student_id " +
            "from student_of_group " +
            "where student_of_group.subject_class_group_id = ?1)", nativeQuery = true)
    List<Student> searchStudentsInGroupById(String groupId, String studentId);

    @Query(value = "select * " +
            "from students " +
            "where students.student_id not in " +
            "(select sg.student_id from subject_classes c join subject_class_groups g on c.subject_class_id = g.subject_class_id join student_of_group sg on g.subject_class_group_id = sg.subject_class_group_id where c.subject_class_id = ?1)", nativeQuery = true)
    List<Student> getStudentsNotInClass(String classId);

    @Query(value = "select * from students where students.student_id like %?2% and students.student_id not in " +
            "(select sg.student_id from subject_classes c join subject_class_groups g on c.subject_class_id = g.subject_class_id join student_of_group sg on g.subject_class_group_id = sg.subject_class_group_id where c.subject_class_id = ?1)", nativeQuery = true)
    List<Student> searchStudentsNotInClassById(String classId, String keyword);
}