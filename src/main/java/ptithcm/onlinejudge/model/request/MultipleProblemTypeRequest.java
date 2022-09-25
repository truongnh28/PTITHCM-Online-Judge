package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptithcm.onlinejudge.model.entity.Problem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleProblemTypeRequest {
    private String[] problemTypeIds;
    private Problem problem;
}
