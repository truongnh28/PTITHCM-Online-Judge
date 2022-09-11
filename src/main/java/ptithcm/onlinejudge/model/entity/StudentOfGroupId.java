package ptithcm.onlinejudge.model.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentOfGroupId implements Serializable {
    private static final long serialVersionUID = -1874855716418719439L;
    @Column(name = "student_id", nullable = false, length = 10)
    private String studentId;

    @Column(name = "subject_class_group_id", nullable = false, length = 10)
    private String subjectClassGroupId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectClassGroupId() {
        return subjectClassGroupId;
    }

    public void setSubjectClassGroupId(String subjectClassGroupId) {
        this.subjectClassGroupId = subjectClassGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentOfGroupId entity = (StudentOfGroupId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.subjectClassGroupId, entity.subjectClassGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, subjectClassGroupId);
    }

}