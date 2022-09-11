package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "test_case")
public class TestCase {
    @Id
    @Column(name = "test_case_id", nullable = false, length = 100)
    private String id;

    @Lob
    @Column(name = "test_case_url", nullable = false)
    private String testCaseUrl;

    @Column(name = "test_case_score")
    private Integer testCaseScore;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestCaseUrl() {
        return testCaseUrl;
    }

    public void setTestCaseUrl(String testCaseUrl) {
        this.testCaseUrl = testCaseUrl;
    }

    public Integer getTestCaseScore() {
        return testCaseScore;
    }

    public void setTestCaseScore(Integer testCaseScore) {
        this.testCaseScore = testCaseScore;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

}