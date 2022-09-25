package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemHasTypeDTO {
    private ProblemDTO problem;
    private ProblemTypeDTO problemType;
}
