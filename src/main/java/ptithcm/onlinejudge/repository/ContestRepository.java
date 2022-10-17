package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Contest;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
    @Query(value = "select * from contests where teacher_id = ?1 order by create_at desc", nativeQuery = true)
    List<Contest> getAllContestsCreateByTeacher(String teacherId);

    @Query(value = "select * from contests where teacher_id = ?1 and (contests.contest_id like %?2% or contests.contest_name like %?2%) order by create_at desc", nativeQuery = true)
    List<Contest> searchContestsCreateByTeacher(String teacherId, String keyword);
    @Query(value = "select * from contests where hide = 0 and teacher_id = ?1 order by create_at desc", nativeQuery = true)
    List<Contest> getContestsActiveByTeacherIdDescByDate(String teacherId);

    @Query(value = "select * from contests where hide = 0 and teacher_id = ?1 and (contests.contest_id like %?2% or contests.contest_name like %?2%) order by create_at desc", nativeQuery = true)
    List<Contest> searchContestsActiveByTeacherIdDescByDate(String teacherId, String keyword);

    @Query(value = "select * from contests where hide = 0 order by create_at desc", nativeQuery = true)
    List<Contest> getContestsActive();

    @Query(value = "select * from contests where hide = 0 and (contests.contest_id like %?1% or contests.contest_name like %?1%) order by create_at desc", nativeQuery = true)
    List<Contest> searchContestsActive(String keyword);

    @Query(value = "select * from contests where hide = 0 order by create_at desc", nativeQuery = true)
    List<Contest> getContestsActiveDescByDate();

    @Query(value = "select * from contests where hide = 0 and (contests.contest_id like %?1% or contests.contest_name like %?1%) order by create_at desc", nativeQuery = true)
    List<Contest> searchContestsActiveDescByDate(String keyword);
}