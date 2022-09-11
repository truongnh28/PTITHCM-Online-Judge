package ptithcm.onlinejudge.model.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @Column(name = "submission_id", nullable = false)
    private Long id;

    @Column(name = "submission_time")
    private Instant submissionTime;

    @Column(name = "submission_score")
    private Integer submissionScore;

    @Column(name = "verdict")
    private Byte verdict;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Instant submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Integer getSubmissionScore() {
        return submissionScore;
    }

    public void setSubmissionScore(Integer submissionScore) {
        this.submissionScore = submissionScore;
    }

    public Byte getVerdict() {
        return verdict;
    }

    public void setVerdict(Byte verdict) {
        this.verdict = verdict;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

}