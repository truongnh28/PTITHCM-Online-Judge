package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.ProblemShowDTO;
import ptithcm.onlinejudge.model.entity.Problem;

@Component
public class ProblemMapperImpl implements ProblemMapper {
    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public Problem dtoToEntity(ProblemDTO dto) {
        if (dto == null) return null;
        Problem entity = new Problem();
        entity.setId(dto.getProblemId());
        entity.setProblemName(dto.getProblemName());
        entity.setProblemUrl(dto.getProblemUrl());
        entity.setProblemCloudinaryId(dto.getProblemCloudinaryId());
        entity.setProblemTimeLimit(dto.getProblemTimeLimit());
        entity.setProblemMemoryLimit(dto.getProblemMemoryLimit());
        entity.setHide(dto.isHide() ? Byte.valueOf("1") : Byte.valueOf("0"));
        entity.setProblemScore(dto.getProblemScore());
        entity.setLevel(levelMapper.dtoToEntity(dto.getLevel()));
        entity.setTeacher(teacherMapper.dtoToEntity(dto.getTeacher()));
        return entity;
    }

    @Override
    public ProblemDTO entityToDTO(Problem entity) {
        if (entity == null) return null;
        ProblemDTO dto = new ProblemDTO();
        dto.setProblemId(entity.getId());
        dto.setProblemName(entity.getProblemName());
        dto.setProblemUrl(entity.getProblemUrl());
        dto.setProblemCloudinaryId(entity.getProblemCloudinaryId());
        dto.setProblemScore(entity.getProblemScore());
        dto.setProblemTimeLimit(entity.getProblemTimeLimit());
        dto.setProblemMemoryLimit(entity.getProblemMemoryLimit());
        dto.setHide(entity.getHide().equals(Byte.valueOf("1")));
        dto.setLevel(levelMapper.entityToDTO(entity.getLevel()));
        dto.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        return dto;
    }

    @Override
    public ProblemShowDTO entityToProblemShowDTO(Problem entity) {
        if (entity == null) return null;
        ProblemShowDTO problemShowDTO = new ProblemShowDTO();
        problemShowDTO.setProblemId(entity.getId());
        problemShowDTO.setProblemName(entity.getProblemName());
        problemShowDTO.setProblemScore(entity.getProblemScore());
        problemShowDTO.setProblemUrl(entity.getProblemUrl());
        problemShowDTO.setHide(entity.getHide() == (byte) 1);
        problemShowDTO.setProblemCloudinaryId(entity.getProblemCloudinaryId());
        problemShowDTO.setProblemTimeLimit(entity.getProblemTimeLimit());
        problemShowDTO.setProblemMemoryLimit(entity.getProblemMemoryLimit());
        problemShowDTO.setLevel(levelMapper.entityToDTO(entity.getLevel()));
        problemShowDTO.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        return problemShowDTO;
    }
}
