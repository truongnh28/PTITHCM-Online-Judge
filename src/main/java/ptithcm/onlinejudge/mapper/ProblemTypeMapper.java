package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.ProblemTypeDTO;
import ptithcm.onlinejudge.dto.ProblemTypeShowDTO;
import ptithcm.onlinejudge.model.entity.ProblemType;

public interface ProblemTypeMapper {
    ProblemType dtoToEntity(ProblemTypeDTO dto);
    ProblemTypeDTO entityToDTO(ProblemType entity);

    ProblemTypeShowDTO entityToShowDTO(ProblemType entity);
}
