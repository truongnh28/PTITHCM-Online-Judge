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
public class StudentOfGroupId implements Serializable {
    private static final long serialVersionUID = -1874855716418719439L;
    @Column(name = "student_id", nullable = false, length = 10)
    private String studentId;

    @Column(name = "subject_class_group_id", nullable = false, length = 50)
    private String subjectClassGroupId;
}