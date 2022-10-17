package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.ProblemTypeDTO;
import ptithcm.onlinejudge.dto.ProblemTypeShowDTO;
import ptithcm.onlinejudge.model.entity.ProblemType;

@Component
public class ProblemTypeMapperImpl implements ProblemTypeMapper {
    @Override
    public ProblemType dtoToEntity(ProblemTypeDTO dto) {
        if (dto == null) return null;
        return new ProblemType(dto.getProblemTypeId(), dto.getProblemTypeName());
    }

    @Override
    public ProblemTypeDTO entityToDTO(ProblemType entity) {
        if (entity == null) return null;
        return new ProblemTypeDTO(entity.getId(), entity.getProblemTypeName());
    }

    @Override
    public ProblemTypeShowDTO entityToShowDTO(ProblemType entity) {
        if (entity == null) return null;
        ProblemTypeShowDTO dto = new ProblemTypeShowDTO();
        dto.setProblemTypeId(entity.getId());
        dto.setProblemTypeName(entity.getProblemTypeName());
        return dto;
    }
}
