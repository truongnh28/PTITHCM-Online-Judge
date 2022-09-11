package ptithcm.onlinejudge.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "problem_type")
public class ProblemType {
    @Id
    @Column(name = "problem_type_id", nullable = false, length = 100)
    private String id;

    @Column(name = "problem_type_name", nullable = false, length = 100)
    private String problemTypeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemTypeName() {
        return problemTypeName;
    }

    public void setProblemTypeName(String problemTypeName) {
        this.problemTypeName = problemTypeName;
    }

}