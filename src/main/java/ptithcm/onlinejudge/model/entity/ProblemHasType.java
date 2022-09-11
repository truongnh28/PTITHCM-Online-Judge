package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "problem_has_type")
public class ProblemHasType {
    @EmbeddedId
    private ProblemHasTypeId id;

    @MapsId("problemId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @MapsId("problemTypeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_type_id", nullable = false)
    private ProblemType problemType;

    public ProblemHasTypeId getId() {
        return id;
    }

    public void setId(ProblemHasTypeId id) {
        this.id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public void setProblemType(ProblemType problemType) {
        this.problemType = problemType;
    }

}