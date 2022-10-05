package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> searchStudentByIdLikeIgnoreCase(String studentId);
}