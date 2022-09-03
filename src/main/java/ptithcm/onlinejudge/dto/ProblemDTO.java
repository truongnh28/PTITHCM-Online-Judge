package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDTO {
    private String problemId;
    private String problemTitle;
    private String problemDescription;
    private int problemTimeLimit;
    private int problemMemoryLimit;
    private int problemScore;
    private LevelDTO level;
    private TeacherDTO author;
    private boolean hide;
    private boolean solved;
}
