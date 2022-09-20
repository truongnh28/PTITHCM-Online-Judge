package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subject_class_groups")
public class SubjectClassGroup {
    @Id
    @Column(name = "subject_class_group_id", nullable = false, length = 10)
    private String id;

    @Column(name = "subject_class_group_name", length = 100)
    private String subjectClassGroupName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_class_id", nullable = false)
    private SubjectClass subjectClass;
}