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
    private int problemTimeLimit;
    private int problemMemoryLimit;
    private int problemScore;
    private LevelDTO level;
    private TeacherDTO author;
    private boolean hide;
    private boolean solved;
}
