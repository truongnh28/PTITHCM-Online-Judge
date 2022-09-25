package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
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
        entity.setSubject(subjectMapper.dtoToEntity(dto.getSubject()));
        return entity;
    }

    @Override
    public SubjectClassDTO entityToDTO(SubjectClass entity) {
        if (entity == null) return null;
        SubjectClassDTO dto = new SubjectClassDTO();
        dto.setSubjectClassId(entity.getId());
        dto.setSubjectClassName(entity.getSubjectClassName());
        dto.setSubject(subjectMapper.entityToDTO(entity.getSubject()));
        return dto;
    }
}
