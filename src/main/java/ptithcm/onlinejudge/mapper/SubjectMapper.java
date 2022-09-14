package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.model.entity.Subject;

public interface SubjectMapper {
    Subject dtoToEntity(SubjectDTO dto);
    SubjectDTO entityToDTO(Subject entity);
}
