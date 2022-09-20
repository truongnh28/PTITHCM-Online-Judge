package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDTO {
    private String id;
    private String problemName;
    private String problemUrl;
    private String problemCloudinaryId;
    private int problemTimeLimit;
    private int problemMemoryLimit;
    private int problemScore;
    private LevelDTO level;
    private TeacherDTO teacher;
    private boolean hide;
}
