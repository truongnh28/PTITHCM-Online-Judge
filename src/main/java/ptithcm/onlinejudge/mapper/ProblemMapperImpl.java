package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.ProblemDTO;
import ptithcm.onlinejudge.dto.ProblemShowDTO;
import ptithcm.onlinejudge.dto.ProblemSolvedDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.Submission;
import ptithcm.onlinejudge.repository.SubmissionRepository;

import java.util.List;

@Component
public class ProblemMapperImpl implements ProblemMapper {
    @Autowired
    private SubmissionRepository submissionRepository;
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
        entity.setHide(dto.getHide());
        entity.setProblemScore(dto.getProblemScore());
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        if (dto.getLevel() != null) entity.setLevel(levelMapper.dtoToEntity(dto.getLevel()));
        if (dto.getTeacher() != null) entity.setTeacher(teacherMapper.dtoToEntity(dto.getTeacher()));
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
        dto.setHide(entity.getHide());
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getLevel() != null) dto.setLevel(levelMapper.entityToDTO(entity.getLevel()));
        if (entity.getTeacher() != null) dto.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        return dto;
    }

    @Override
    public ProblemSolvedDTO entityToSolvedDTO(Problem entity) {
        if (entity == null) return null;
        ProblemSolvedDTO dto = new ProblemSolvedDTO();
        dto.setProblemId(entity.getId());
        dto.setProblemName(entity.getProblemName());
        dto.setProblemUrl(entity.getProblemUrl());
        dto.setProblemCloudinaryId(entity.getProblemCloudinaryId());
        dto.setProblemScore(entity.getProblemScore());
        dto.setProblemTimeLimit(entity.getProblemTimeLimit());
        dto.setProblemMemoryLimit(entity.getProblemMemoryLimit());
        dto.setHide(entity.getHide());
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getLevel() != null) dto.setLevel(levelMapper.entityToDTO(entity.getLevel()));
        if (entity.getTeacher() != null) dto.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
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
        problemShowDTO.setHide(entity.getHide());
        problemShowDTO.setProblemCloudinaryId(entity.getProblemCloudinaryId());
        problemShowDTO.setProblemTimeLimit(entity.getProblemTimeLimit());
        problemShowDTO.setProblemMemoryLimit(entity.getProblemMemoryLimit());
        problemShowDTO.setLevel(levelMapper.entityToDTO(entity.getLevel()));
        problemShowDTO.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        return problemShowDTO;
    }
}
