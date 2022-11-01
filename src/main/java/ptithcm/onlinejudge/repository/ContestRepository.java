package ptithcm.onlinejudge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Contest;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
    @Query(value = "select * from contests where teacher_id = ?1 order by create_at desc",
            countQuery = "select count(*) from contests where teacher_id = ?1 order by create_at desc",
            nativeQuery = true)
    Page<Contest> getAllContestsCreateByTeacher(String teacherId, Pageable pageable);

    @Query(value = "select * from contests where teacher_id = ?1 and (lower(contests.contest_id) like lower(?2) or lower(contests.contest_name) like lower(?2)) order by create_at desc",
            countQuery = "select count(*) from contests where teacher_id = ?1 and (lower(contests.contest_id) like lower(?2) or lower(contests.contest_name) like lower(?2)) order by create_at desc",
            nativeQuery = true)
    Page<Contest> searchContestsCreateByTeacher(String teacherId, String keyword, Pageable pageable);
    @Query(value = "select * from contests where hide = 0 and teacher_id = ?1 order by create_at desc", nativeQuery = true)
    List<Contest> getContestsActiveByTeacherIdDescByDate(String teacherId);

    @Query(value = "select * from contests where hide = 0 and teacher_id = ?1 and (contests.contest_id like %?2% or contests.contest_name like %?2%) order by create_at desc", nativeQuery = true)
    List<Contest> searchContestsActiveByTeacherIdDescByDate(String teacherId, String keyword);

    @Query(value = "select * from contests where hide = 0 order by create_at desc",
            countQuery = "select count(*) from contests where hide = 0 order by create_at desc",
            nativeQuery = true)
    Page<Contest> getContestsActive(Pageable pageable);

    @Query(value = "select * from contests where hide = 0 and (lower(contests.contest_id) like lower(?1) or lower(contests.contest_name) like lower(?1)) order by create_at desc",
            countQuery = "select count(*) from contests where hide = 0 and (lower(contests.contest_id) like lower(?1) or lower(contests.contest_name) like lower(?1)) order by create_at desc",
            nativeQuery = true)
    Page<Contest> searchContestsActive(String keyword, Pageable pageable);

    @Query(value = "select * from contests where hide = 0 order by create_at desc",
            countQuery = "select count(*) from contests where hide = 0 order by create_at desc",
            nativeQuery = true)
    Page<Contest> getContestsActiveDescByDate(Pageable pageable);

    @Query(value = "select * from contests where hide = 0 and (lower(contests.contest_id) like lower(?1) or lower(contests.contest_name) like lower(?1)) order by create_at desc",
            countQuery = "select count(*) from contests where hide = 0 and (lower(contests.contest_id) like lower(?1) or lower(contests.contest_name) like lower(?1)) order by create_at desc",
            nativeQuery = true)
    Page<Contest> searchContestsActiveDescByDate(String keyword, Pageable pageable);
}