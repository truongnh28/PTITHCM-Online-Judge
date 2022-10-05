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

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @Column(name = "hide")
    private Byte hide;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

}