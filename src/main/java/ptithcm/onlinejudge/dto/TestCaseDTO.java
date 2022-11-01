package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDTO {
    private String testCaseId;
    private String testCaseInput;
    private String testCaseOutput;
    private ProblemDTO problem;
}
