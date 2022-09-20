package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}