package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptithcm.onlinejudge.model.entity.Teacher;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemRequest {
    private String problemId;
    private String problemName;
    private int score;
    private String teacherId;
}
