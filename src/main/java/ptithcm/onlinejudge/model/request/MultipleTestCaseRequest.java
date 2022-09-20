package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleTestCaseRequest {
    private String[] testInPaths;
    private String[] testOutPaths;
    private String problemId;
}
