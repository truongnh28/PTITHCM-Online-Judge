package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "subject_classes")
public class SubjectClass {
    @Id
    @Column(name = "subject_class_id", nullable = false, length = 10)
    private String id;

    @Column(name = "subject_class_name", length = 100)
    private String subjectClassName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public SubjectClass(String id, String subjectClassName, Subject subject) {
        this.id = id;
        this.subjectClassName = subjectClassName;
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectClassName() {
        return subjectClassName;
    }

    public void setSubjectClassName(String subjectClassName) {
        this.subjectClassName = subjectClassName;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}