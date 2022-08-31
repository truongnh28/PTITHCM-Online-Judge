package ptithcm.onlinejudge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRequest {
    private String problemId;
    private String problemName;
    private int score;
}
