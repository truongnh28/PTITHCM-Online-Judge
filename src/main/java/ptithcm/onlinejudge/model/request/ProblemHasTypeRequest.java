package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.ProblemType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemHasTypeRequest {
    private Problem problem;
    private ProblemType problemType;
}
