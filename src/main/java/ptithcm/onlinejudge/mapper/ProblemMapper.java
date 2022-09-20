package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.model.entity.Problem;

public interface ProblemMapper {
    Problem dtoToEntity(ProblemDTO dto);
    ProblemDTO entityToDTO(Problem entity);
}
