package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.RankList;
import ptithcm.onlinejudge.model.entity.RankListId;

public interface RankListRepository extends JpaRepository<RankList, RankListId> {
}