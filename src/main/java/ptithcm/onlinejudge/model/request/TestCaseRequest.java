package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.onlinejudge.model.entity.Problem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseRequest {
    private String id;
    private String testCaseIn;
    private String testCaseOut;
    private Integer testCaseScore;
    private Problem problem;
}
