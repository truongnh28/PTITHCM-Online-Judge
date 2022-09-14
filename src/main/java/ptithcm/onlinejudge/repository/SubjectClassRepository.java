package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptithcm.onlinejudge.model.entity.SubjectClass;

import java.util.List;

public interface SubjectClassRepository extends JpaRepository<SubjectClass, String> {
    @Query(value = "SELECT * FROM subject_classes WHERE subject_classes.subject_id = ?1", nativeQuery = true)
    List<SubjectClass> getSubjectClassBySubjectId(String subjectId);
}