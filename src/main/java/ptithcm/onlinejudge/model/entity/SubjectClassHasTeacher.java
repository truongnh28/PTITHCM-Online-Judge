package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subject_class_has_teacher")
public class SubjectClassHasTeacher {
    @EmbeddedId
    private SubjectClassHasTeacherId id;

    @MapsId("subjectClassId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_class_id", nullable = false)
    private SubjectClass subjectClass;

    @MapsId("teacherId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
}
