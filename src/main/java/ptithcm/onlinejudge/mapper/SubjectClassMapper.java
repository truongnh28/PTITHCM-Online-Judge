package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.model.entity.SubjectClass;

public interface SubjectClassMapper {
    SubjectClass dtoToEntity(SubjectClassDTO dto);
    SubjectClassDTO entityToDTO(SubjectClass entity);
}
