package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.LevelDTO;
import ptithcm.onlinejudge.dto.LevelShowDTO;
import ptithcm.onlinejudge.model.entity.Level;

public interface LevelMapper {
    Level dtoToEntity(LevelDTO dto);
    LevelDTO entityToDTO(Level entity);
    LevelShowDTO entityToShowSTO(Level entity);
}
