package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}