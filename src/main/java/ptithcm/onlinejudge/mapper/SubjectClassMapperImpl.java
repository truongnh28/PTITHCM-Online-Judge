package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.SubjectClass;

@Component
public class SubjectClassMapperImpl implements SubjectClassMapper{
    @Autowired
    private SubjectMapper subjectMapper;
    @Override
    public SubjectClass dtoToEntity(SubjectClassDTO dto) {
        if (dto == null) return null;
        SubjectClass entity = new SubjectClass();
        entity.setId(dto.getSubjectClassId());
        entity.setSubjectClassName(dto.getSubjectClassName());
        entity.setHide(dto.getHide());
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        if (dto.getSubject() != null) entity.setSubject(subjectMapper.dtoToEntity(dto.getSubject()));
        return entity;
    }

    @Override
    public SubjectClassDTO entityToDTO(SubjectClass entity) {
        if (entity == null) return null;
        SubjectClassDTO dto = new SubjectClassDTO();
        dto.setSubjectClassId(entity.getId());
        dto.setSubjectClassName(entity.getSubjectClassName());
        dto.setHide(entity.getHide());
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getSubject() != null) dto.setSubject(subjectMapper.entityToDTO(entity.getSubject()));
        return dto;
    }
}
