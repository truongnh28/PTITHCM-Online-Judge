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

    @Column(name = "score", nullable = false)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @Column(name = "problem_cloudinary_id", length = 100)
    private String problemCloudinaryId;

    @Lob
    @Column(name = "problem_url")
    private String problemUrl;

    public Problem(String id, String problemName, Integer score, Contest contest, String problemCloudinaryId, String problemUrl) {
        this.id = id;
        this.problemName = problemName;
        this.score = score;
        this.contest = contest;
        this.problemCloudinaryId = problemCloudinaryId;
        this.problemUrl = problemUrl;
    }

    public String getProblemUrl() {
        return problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }

    public String getProblemCloudinaryId() {
        return problemCloudinaryId;
    }

    public void setProblemCloudinaryId(String problemCloudinaryId) {
        this.problemCloudinaryId = problemCloudinaryId;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

}