package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDetailDTO {
    private String sourceCodeId;
    private String sourceCode;
    private int timeExec;
    private int memoryUsed;
    private String studentId;
    private String language;
    private String status;
}
