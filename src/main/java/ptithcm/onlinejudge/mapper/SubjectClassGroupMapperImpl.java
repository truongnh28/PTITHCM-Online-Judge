package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

@Component
public class SubjectClassGroupMapperImpl implements SubjectClassGroupMapper {
    @Autowired
    private SubjectClassMapper subjectClassMapper;
    @Override
    public SubjectClassGroup dtoToEntity(SubjectClassGroupDTO dto) {
        if (dto == null) return null;
        SubjectClassGroup entity = new SubjectClassGroup();
        entity.setId(dto.getSubjectClassGroupId());
        entity.setSubjectClassGroupName(dto.getSubjectClassGroupName());
        entity.setSubjectClass(subjectClassMapper.dtoToEntity(dto.getSubjectClass()));
        return entity;
    }

    @Override
    public SubjectClassGroupDTO entityToDTO(SubjectClassGroup entity) {
        if (entity == null) return null;
        SubjectClassGroupDTO dto = new SubjectClassGroupDTO();
        dto.setSubjectClassGroupId(entity.getId());
        dto.setSubjectClassGroupName(entity.getSubjectClassGroupName());
        dto.setSubjectClass(subjectClassMapper.entityToDTO(entity.getSubjectClass()));
        return dto;
    }
}
