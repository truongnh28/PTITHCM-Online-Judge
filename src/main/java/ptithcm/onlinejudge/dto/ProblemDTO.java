package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDTO {
    private String id;
    private String title;
    private String description;
    private int timeLimit;
    private int memoryLimit;
    private int score;
    private TeacherDTO author;
    private boolean hide;
    private boolean solved;
}
