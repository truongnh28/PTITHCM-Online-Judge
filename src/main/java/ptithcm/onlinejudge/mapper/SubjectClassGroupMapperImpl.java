package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

@Component
public class SubjectClassGroupMapperImpl implements SubjectClassGroupMapper {
    @Autowired
    private SubjectClassMapper subjectClassMapper;
    @Override
    public SubjectClassGroup dtoToEntity(SubjectClassGroupDTO dto) {
        if (dto == null) return null;
        SubjectClassGroup entity = new SubjectClassGroup();
        entity.setId(dto.getGroupId());
        entity.setSubjectClassGroupName(dto.getGroupName());
        entity.setHide(dto.getHide());
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        if (dto.getSubjectClass() != null) entity.setSubjectClass(subjectClassMapper.dtoToEntity(dto.getSubjectClass()));
        return entity;
    }

    @Override
    public SubjectClassGroupDTO entityToDTO(SubjectClassGroup entity) {
        if (entity == null) return null;
        SubjectClassGroupDTO dto = new SubjectClassGroupDTO();
        dto.setGroupId(entity.getId());
        dto.setGroupName(entity.getSubjectClassGroupName());
        dto.setHide(entity.getHide());
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getSubjectClass() != null) dto.setSubjectClass(subjectClassMapper.entityToDTO(entity.getSubjectClass()));
        return dto;
    }
}
