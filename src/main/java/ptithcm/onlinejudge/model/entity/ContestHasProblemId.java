package ptithcm.onlinejudge.model.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContestHasProblemId implements Serializable {
    private static final long serialVersionUID = 2467483236707015568L;
    @Column(name = "contest_id", nullable = false, length = 100)
    private String contestId;

    @Column(name = "problem_id", nullable = false, length = 100)
    private String problemId;

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ContestHasProblemId entity = (ContestHasProblemId) o;
        return Objects.equals(this.contestId, entity.contestId) &&
                Objects.equals(this.problemId, entity.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestId, problemId);
    }

}