package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.model.entity.Subject;

@Component
public class SubjectMapperImpl implements SubjectMapper {
    @Override
    public Subject dtoToEntity(SubjectDTO dto) {
        if (dto == null) return null;
        Subject entity = new Subject();
        entity.setId(dto.getSubjectId());
        entity.setSubjectName(dto.getSubjectName());
        return entity;
    }

    @Override
    public SubjectDTO entityToDTO(Subject entity) {
        if (entity == null) return null;
        SubjectDTO dto = new SubjectDTO();
        dto.setSubjectId(entity.getId());
        dto.setSubjectName(entity.getSubjectName());
        return dto;
    }
}
