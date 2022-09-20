package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProblemHasTypeId implements Serializable {
    private static final long serialVersionUID = 1254744264135375536L;
    @Column(name = "problem_id", nullable = false, length = 100)
    private String problemId;

    @Column(name = "problem_type_id", nullable = false, length = 100)
    private String problemTypeId;
}