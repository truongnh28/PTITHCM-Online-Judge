package ptithcm.onlinejudge.model.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupHasContestId implements Serializable {
    private static final long serialVersionUID = 8749372755092370595L;
    @Column(name = "contest_id", nullable = false, length = 100)
    private String contestId;

    @Column(name = "subject_class_group_id", nullable = false, length = 10)
    private String subjectClassGroupId;

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
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
        GroupHasContestId entity = (GroupHasContestId) o;
        return Objects.equals(this.contestId, entity.contestId) &&
                Objects.equals(this.subjectClassGroupId, entity.subjectClassGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestId, subjectClassGroupId);
    }

}