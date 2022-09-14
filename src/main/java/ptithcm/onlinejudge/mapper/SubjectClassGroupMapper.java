package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;

public interface SubjectClassGroupMapper {
    SubjectClassGroup dtoToEntity(SubjectClassGroupDTO dto);
    SubjectClassGroupDTO entityToDTO(SubjectClassGroup entity);
}
