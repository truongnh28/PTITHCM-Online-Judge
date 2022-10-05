package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, String> {
    @Query(value = "select * from subjects where subjects.subject_id like %?1% or subjects.subject_name like %?1%", nativeQuery = true)
    List<Subject> searchSubjectByIdOrName(String keyword);
}