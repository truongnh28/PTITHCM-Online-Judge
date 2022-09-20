package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContestHasProblemId implements Serializable {
    private static final long serialVersionUID = 2467483236707015568L;
    @Column(name = "contest_id", nullable = false, length = 100)
    private String contestId;

    @Column(name = "problem_id", nullable = false, length = 100)
    private String problemId;

}