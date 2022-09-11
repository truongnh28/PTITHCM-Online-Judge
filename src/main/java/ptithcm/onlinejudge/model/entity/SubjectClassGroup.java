package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

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

    public SubjectClassGroup(String id, String subjectClassGroupName, SubjectClass subjectClass) {
        this.id = id;
        this.subjectClassGroupName = subjectClassGroupName;
        this.subjectClass = subjectClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectClassGroupName() {
        return subjectClassGroupName;
    }

    public void setSubjectClassGroupName(String subjectClassGroupName) {
        this.subjectClassGroupName = subjectClassGroupName;
    }

    public SubjectClass getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(SubjectClass subjectClass) {
        this.subjectClass = subjectClass;
    }

}