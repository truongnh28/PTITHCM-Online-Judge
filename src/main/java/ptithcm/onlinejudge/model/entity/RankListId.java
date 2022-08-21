package ptithcm.onlinejudge.model.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RankListId implements Serializable {
    private static final long serialVersionUID = -8852400902257818174L;
    @Column(name = "user_id", nullable = false, length = 10)
    private String userId;

    @Column(name = "contest_id", nullable = false, length = 100)
    private String contestId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RankListId entity = (RankListId) o;
        return Objects.equals(this.contestId, entity.contestId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestId, userId);
    }

}