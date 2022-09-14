package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.repository.SubjectRepository;

import java.util.Optional;

@Component
public class SubjectClassMapperImpl implements SubjectClassMapper{
    @Autowired
    private SubjectRepository subjectRepository;
    @Override
    public SubjectClass dtoToEntity(SubjectClassDTO dto) {
        if (dto == null)
            return null;
        SubjectClass entity = new SubjectClass();
        entity.setId(dto.getSubjectClassId());
        entity.setSubjectClassName(dto.getSubjectClassName());
        Optional<Subject> foundSubject = subjectRepository.findById(dto.getSubjectId());
        if (foundSubject.isEmpty())
            return null;
        entity.setSubject(foundSubject.get());
        return entity;
    }

    @Override
    public SubjectClassDTO entityToDTO(SubjectClass entity) {
        if (entity == null)
            return null;
        SubjectClassDTO dto = new SubjectClassDTO();
        dto.setSubjectClassId(entity.getId());
        dto.setSubjectClassName(entity.getSubjectClassName());
        dto.setSubjectId(entity.getSubject().getId());
        return dto;
    }
}
