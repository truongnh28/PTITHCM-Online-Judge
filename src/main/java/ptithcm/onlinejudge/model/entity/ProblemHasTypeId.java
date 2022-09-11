package ptithcm.onlinejudge.model.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProblemHasTypeId implements Serializable {
    private static final long serialVersionUID = 1254744264135375536L;
    @Column(name = "problem_id", nullable = false, length = 100)
    private String problemId;

    @Column(name = "problem_type_id", nullable = false, length = 100)
    private String problemTypeId;

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getProblemTypeId() {
        return problemTypeId;
    }

    public void setProblemTypeId(String problemTypeId) {
        this.problemTypeId = problemTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProblemHasTypeId entity = (ProblemHasTypeId) o;
        return Objects.equals(this.problemTypeId, entity.problemTypeId) &&
                Objects.equals(this.problemId, entity.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemTypeId, problemId);
    }

}