package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectClassGroupDTO;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.entity.SubjectClassGroup;
import ptithcm.onlinejudge.repository.SubjectClassRepository;

import java.util.Optional;

@Component
public class SubjectClassGroupMapperImpl implements SubjectClassGroupMapper {
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Override
    public SubjectClassGroup dtoToEntity(SubjectClassGroupDTO dto) {
        if (dto == null) return null;
        SubjectClassGroup entity = new SubjectClassGroup();
        entity.setId(dto.getSubjectClassGroupId());
        entity.setSubjectClassGroupName(dto.getSubjectClassGroupName());
        Optional<SubjectClass> foundClass = subjectClassRepository.findById(dto.getSubjectClassId());
        if (foundClass.isEmpty()) return null;
        entity.setSubjectClass(foundClass.get());
        return entity;
    }

    @Override
    public SubjectClassGroupDTO entityToDTO(SubjectClassGroup entity) {
        if (entity == null) return null;
        SubjectClassGroupDTO dto = new SubjectClassGroupDTO();
        dto.setSubjectClassGroupId(entity.getId());
        dto.setSubjectClassGroupName(entity.getSubjectClassGroupName());
        dto.setSubjectClassId(entity.getSubjectClass().getId());
        return dto;
    }
}
