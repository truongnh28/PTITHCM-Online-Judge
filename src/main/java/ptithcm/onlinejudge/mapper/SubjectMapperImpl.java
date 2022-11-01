package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Subject;

@Component
public class SubjectMapperImpl implements SubjectMapper {
    @Override
    public Subject dtoToEntity(SubjectDTO dto) {
        if (dto == null) return null;
        Subject entity = new Subject();
        entity.setId(dto.getSubjectId());
        entity.setSubjectName(dto.getSubjectName());
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        if (dto.getHide() != null) entity.setHide(dto.getHide());
        return entity;
    }

    @Override
    public SubjectDTO entityToDTO(Subject entity) {
        if (entity == null) return null;
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectId(entity.getId());
        dto.setSubjectName(entity.getSubjectName());
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getHide() != null) dto.setHide(entity.getHide());
        return dto;
    }
}
