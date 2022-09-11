package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @Column(name = "problem_id", nullable = false, length = 100)
    private String id;

    @Column(name = "problem_name", length = 100)
    private String problemName;

    @Column(name = "problem_cloudinary_id", length = 100)
    private String problemCloudinaryId;

    @Lob
    @Column(name = "problem_url")
    private String problemUrl;

    @Column(name = "problem_score")
    private Integer problemScore;

    @Column(name = "hide")
    private Byte hide;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    public Problem(String id, String problemName, String problemCloudinaryId, String problemUrl, Integer problemScore, Byte hide, Teacher teacher) {
        this.id = id;
        this.problemName = problemName;
        this.problemCloudinaryId = problemCloudinaryId;
        this.problemUrl = problemUrl;
        this.problemScore = problemScore;
        this.hide = hide;
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemCloudinaryId() {
        return problemCloudinaryId;
    }

    public void setProblemCloudinaryId(String problemCloudinaryId) {
        this.problemCloudinaryId = problemCloudinaryId;
    }

    public String getProblemUrl() {
        return problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }

    public Integer getProblemScore() {
        return problemScore;
    }

    public void setProblemScore(Integer problemScore) {
        this.problemScore = problemScore;
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