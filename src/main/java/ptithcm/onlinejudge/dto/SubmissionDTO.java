package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {
    private String username;
    private String timeSubmit;
    private String status;
    private String problemId;
    private int timeExecute;
    private int memoryUsed;
    private String language;
    private String codeId;
}
