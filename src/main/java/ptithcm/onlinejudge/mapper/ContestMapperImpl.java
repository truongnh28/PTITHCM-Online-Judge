package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.ContestDTO;
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
        entity.setHide(dto.isHide() ? Byte.valueOf("1") : Byte.valueOf("0"));
        entity.setContestStart(TimeHelper.convertStringToInstance(dto.getContestStart()));
        entity.setContestEnd(TimeHelper.convertStringToInstance(dto.getContestEnd()));
        entity.setTeacher(teacherMapper.dtoToEntity(dto.getTeacher()));
        return entity;
    }

    @Override
    public ContestDTO entityToDTO(Contest entity) {
        if (entity == null) return null;
        ContestDTO dto = new ContestDTO();
        dto.setContestId(entity.getId());
        dto.setContestName(entity.getContestName());
        dto.setHide(entity.getHide().equals(Byte.valueOf("1")));
        dto.setContestStart(entity.getContestStart().toString());
        dto.setContestEnd(entity.getContestEnd().toString());
        dto.setTeacher(teacherMapper.entityToDTO(entity.getTeacher()));
        return dto;
    }

    @Override
    public ContestRequest dtoToRequest(ContestDTO dto) {
        if (dto == null) return null;
        ContestRequest request = new ContestRequest();
        request.setContestId(dto.getContestId());
        request.setContestStartTime(dto.getContestStart());
        request.setContestEndTime(dto.getContestEnd());
        request.setContestName(dto.getContestName());
        if (dto.getTeacher() != null)
            request.setTeacherId(dto.getTeacher().getTeacherId());
        return request;
    }
}
