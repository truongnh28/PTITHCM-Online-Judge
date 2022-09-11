package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

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

    public StudentOfGroupId getId() {
        return id;
    }

    public void setId(StudentOfGroupId id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SubjectClassGroup getSubjectClassGroup() {
        return subjectClassGroup;
    }

    public void setSubjectClassGroup(SubjectClassGroup subjectClassGroup) {
        this.subjectClassGroup = subjectClassGroup;
    }

}