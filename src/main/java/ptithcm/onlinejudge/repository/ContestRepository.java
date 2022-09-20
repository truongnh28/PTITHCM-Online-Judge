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
    @Query(value = "select * from contests where hide = 0 and teacher_id = ?1", nativeQuery = true)
    List<Contest> getContestsActiveByTeacherId(String teacherId);

    @Query(value = "select * " +
            "from contests " +
            "where contests.hide = 0 " +
            "and contests.teacher_id = ?1 " +
            "and contests.contest_id in " +
            "(select group_has_contest from group_has_co)", nativeQuery = true)
    List<Contest> getContestsActiveByTeacherIdAndGroupId(String teacherId, String groupId);
}