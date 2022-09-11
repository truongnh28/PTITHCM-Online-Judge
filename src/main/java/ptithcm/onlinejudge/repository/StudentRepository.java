package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
}