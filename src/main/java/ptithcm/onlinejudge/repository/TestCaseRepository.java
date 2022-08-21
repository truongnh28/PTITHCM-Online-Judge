package ptithcm.onlinejudge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptithcm.onlinejudge.model.entity.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, String> {
}