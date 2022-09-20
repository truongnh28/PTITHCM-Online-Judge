package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.model.entity.Contest;

public interface ContestMapper {
    Contest dtoToEntity(ContestDTO dto);
    ContestDTO entityToDTO(Contest entity);
}
