package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_of_group")
public class StudentOfGroup {
    @EmbeddedId
    private StudentOfGroupId id;

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @MapsId("subjectClassGroupId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_class_group_id", nullable = false)
    private SubjectClassGroup subjectClassGroup;
}