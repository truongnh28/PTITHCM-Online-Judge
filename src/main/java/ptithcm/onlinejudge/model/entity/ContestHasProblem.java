package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "contest_has_problem")
public class ContestHasProblem {
    @EmbeddedId
    private ContestHasProblemId id;

    @MapsId("contestId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    public ContestHasProblemId getId() {
        return id;
    }

    public void setId(ContestHasProblemId id) {
        this.id = id;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

}