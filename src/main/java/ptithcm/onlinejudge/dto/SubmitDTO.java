package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDTO {
    private String studentId;
    private String problemId;
    private String sourceCode;
    private String language;
}
