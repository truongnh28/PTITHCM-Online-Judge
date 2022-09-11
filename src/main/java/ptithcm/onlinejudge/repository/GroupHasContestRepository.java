package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.GroupHasContest;
import ptithcm.onlinejudge.model.entity.GroupHasContestId;

public interface GroupHasContestRepository extends JpaRepository<GroupHasContest, GroupHasContestId> {
}