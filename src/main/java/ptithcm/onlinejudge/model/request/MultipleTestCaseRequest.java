package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.onlinejudge.model.entity.Problem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleTestCaseRequest {
    private String[] testInPaths;
    private String[] testOutPaths;
    private Problem problem;
}
