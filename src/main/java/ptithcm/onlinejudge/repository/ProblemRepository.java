package ptithcm.onlinejudge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptithcm.onlinejudge.model.entity.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, String> {

}
