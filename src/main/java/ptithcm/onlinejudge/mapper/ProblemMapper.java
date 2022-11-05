package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.ProblemShowDTO;
import ptithcm.onlinejudge.dto.ProblemSolvedDTO;
import ptithcm.onlinejudge.model.entity.Problem;

public interface ProblemMapper {
    Problem dtoToEntity(ProblemDTO dto);
    ProblemDTO entityToDTO(Problem entity);
    ProblemSolvedDTO entityToSolvedDTO(Problem entity);
    ProblemShowDTO entityToProblemShowDTO(Problem entity);
}
