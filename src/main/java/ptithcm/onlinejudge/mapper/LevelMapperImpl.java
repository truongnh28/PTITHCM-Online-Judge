package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.LevelDTO;
import ptithcm.onlinejudge.dto.LevelShowDTO;
import ptithcm.onlinejudge.model.entity.Level;

@Component
public class LevelMapperImpl implements LevelMapper {
    @Override
    public Level dtoToEntity(LevelDTO dto) {
        if (dto == null) return null;
        return new Level((byte) dto.getLevelId(), dto.getLevelName());
    }

    @Override
    public LevelDTO entityToDTO(Level entity) {
        if (entity == null) return null;
        return new LevelDTO(entity.getId(), entity.getLevelName());
    }

    @Override
    public LevelShowDTO entityToShowSTO(Level entity) {
        if (entity == null) return null;
        LevelShowDTO dto = new LevelShowDTO();
        dto.setLevelId(entity.getId());
        dto.setLevelName(entity.getLevelName());
        return dto;
    }
}
