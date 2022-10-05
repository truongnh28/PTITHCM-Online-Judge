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
public class GroupHasContestId implements Serializable {
    private static final long serialVersionUID = 8749372755092370595L;
    @Column(name = "contest_id", nullable = false, length = 100)
    private String contestId;

    @Column(name = "subject_class_group_id", nullable = false, length = 50)
    private String subjectClassGroupId;
}