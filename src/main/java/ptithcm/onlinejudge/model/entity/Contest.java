package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "contests")
public class Contest {
    @Id
    @Column(name = "contest_id", nullable = false, length = 100)
    private String id;

    @Column(name = "contest_name", nullable = false, length = 100)
    private String contestName;

    @Column(name = "contest_start", nullable = false)
    private Instant contestStart;

    @Column(name = "contest_end", nullable = false)
    private Instant contestEnd;

    @Column(name = "hide")
    private Byte hide;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    public Contest(String id, String contestName, Instant contestStart, Instant contestEnd, Byte hide, Teacher teacher) {
        this.id = id;
        this.contestName = contestName;
        this.contestStart = contestStart;
        this.contestEnd = contestEnd;
        this.hide = hide;
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public Instant getContestStart() {
        return contestStart;
    }

    public void setContestStart(Instant contestStart) {
        this.contestStart = contestStart;
    }

    public Instant getContestEnd() {
        return contestEnd;
    }

    public void setContestEnd(Instant contestEnd) {
        this.contestEnd = contestEnd;
    }

    public Byte getHide() {
        return hide;
    }

    public void setHide(Byte hide) {
        this.hide = hide;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

}