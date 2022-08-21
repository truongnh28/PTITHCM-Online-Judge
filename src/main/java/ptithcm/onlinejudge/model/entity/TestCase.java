package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "test_case")
public class TestCase {
    @Id
    @Column(name = "test_case_id", nullable = false, length = 100)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

}