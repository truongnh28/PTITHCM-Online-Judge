package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.ContestDTO;
import ptithcm.onlinejudge.dto.ContestDetailDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Contest;
import ptithcm.onlinejudge.model.request.ContestRequest;


@Component
public class ContestMapperImpl implements ContestMapper {
    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public Contest dtoToEntity(ContestDTO dto) {
        if (dto == null) return null;
        Contest entity = new Contest();
        entity.setId(dto.getContestId());
        entity.setContestName(dto.getContestName());
        entity.setHide(dto.getHide());
        if (dto.getContestStart() != null) entity.setContestStart(TimeHelper.convertStringToInstance(dto.getContestStart()));
        if (dto.getContestEnd() != null) entity.setContestEnd(TimeHelper.convertStringToInstance(dto.getContestEnd()));
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        if (dto.getTeacher() != null) entity.setTeacher(teacherMapper.dtoToEntity(dto.getTeacher()));
        return entity;
    }

    @Override
    public ContestDTO entityToDTO(Contest entity) {
        if (entity == null) return null;
        ContestDTO dto = new ContestDTO();
        dto.setContestId(entity.getId());
        dto.setContestName(entity.getContestName());
        dto.setHide(entity.getHide());
        if (entity.getContestStart() != null) dto.setContestStart(TimeHelper.convertInstantToString(entity.getContestStart()));
        if (entity.getContestEnd() != null) dto.setContestEnd(TimeHelper.convertInstantToString(entity.getContestEnd()));
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getTeacher() != null) dto.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        return dto;
    }

    @Override
    public ContestDetailDTO entityToDetailDTO(Contest entity) {
        if (entity == null) return null;
        ContestDetailDTO dto = new ContestDetailDTO();
        dto.setContestId(entity.getId());
        dto.setContestName(entity.getContestName());
        dto.setHide(entity.getHide());
        dto.setContestStart(TimeHelper.convertInstantToString(entity.getContestStart()));
        dto.setContestEnd(TimeHelper.convertInstantToString(entity.getContestEnd()));
        dto.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (TimeHelper.nowIsAfter(entity.getContestEnd()))
            dto.setVerdict((byte) 2); // ended
        else if (TimeHelper.nowIsAfter(entity.getContestStart()))
            dto.setVerdict((byte) 1); // running
        else
            dto.setVerdict((byte) 0); // upcoming
        return dto;
    }
}
