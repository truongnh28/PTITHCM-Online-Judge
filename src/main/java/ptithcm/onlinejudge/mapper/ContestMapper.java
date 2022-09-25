package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.request.ContestRequest;

public interface ContestMapper {
    Contest dtoToEntity(ContestDTO dto);
    ContestDTO entityToDTO(Contest entity);

    ContestRequest dtoToRequest(ContestDTO dto);
}
