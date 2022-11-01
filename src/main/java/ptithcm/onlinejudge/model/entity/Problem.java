package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "problem_time_limit")
    private Integer problemTimeLimit;

    @Column(name = "problem_memory_limit")
    private Integer problemMemoryLimit;

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @Column(name = "hide")
    private Byte hide;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

}