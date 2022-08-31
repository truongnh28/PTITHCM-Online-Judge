package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {
    private String studentId;
    private String problemId;
    private String timeSubmit;
    private String status;
    private int timeExecute;
    private int memoryUsed;
    private String language;
    private String sourceCodeId;
    private String sourceCodeURL;
}
