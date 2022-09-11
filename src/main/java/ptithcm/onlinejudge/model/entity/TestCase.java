package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "test_case")
public class TestCase {
    @Id
    @Column(name = "test_case_id", nullable = false, length = 100)
    private String id;

    @Lob
    @Column(name = "test_case_in", nullable = false)
    private String testCaseIn;

    @Lob
    @Column(name = "test_case_out", nullable = false)
    private String testCaseOut;

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

    public String getTestCaseIn() {
        return testCaseIn;
    }

    public void setTestCaseIn(String testCaseIn) {
        this.testCaseIn = testCaseIn;
    }

    public String getTestCaseOut() {
        return testCaseOut;
    }

    public void setTestCaseOut(String testCaseOut) {
        this.testCaseOut = testCaseOut;
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