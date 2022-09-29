package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {
    private String submissionId;
    private StudentDTO student;
    private ProblemDTO problem;
    private String submissionTime;
    private Integer submissionScore;
    private Byte verdict;
}
