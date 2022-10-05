package ptithcm.onlinejudge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.SubjectClassHasTeacher;
import ptithcm.onlinejudge.model.entity.SubjectClassHasTeacherId;

@Repository
public interface SubjectClassHasTeacherRepository extends JpaRepository<SubjectClassHasTeacher, SubjectClassHasTeacherId> {
}
