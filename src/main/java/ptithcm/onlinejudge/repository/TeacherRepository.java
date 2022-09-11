package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
}