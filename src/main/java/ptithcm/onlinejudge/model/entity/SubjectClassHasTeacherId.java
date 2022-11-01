package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectClassHasTeacherId implements Serializable {
    private static final long serialVersionUID = -1874855716418719439L;

    @Column(name = "subject_class_id", nullable = false, length = 10)
    private String subjectClassId;

    @Column(name = "teacher_id", nullable = false, length = 10)
    private String teacherId;
}
