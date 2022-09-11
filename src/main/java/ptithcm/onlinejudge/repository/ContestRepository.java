package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Contest;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
    @Query(value = "select * from contests where teacher_id = ?1", nativeQuery = true)
    List<Contest> getContestsByTeacher(String teacherId);
}