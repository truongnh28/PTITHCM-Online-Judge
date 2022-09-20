package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.model.entity.Teacher;

@Component
public class TeacherMapperImpl implements TeacherMapper {
    @Override
    public Teacher dtoToEntity(TeacherDTO dto) {
        if (dto == null) return null;
        Teacher entity = new Teacher();
        entity.setId(dto.getTeacherId());
        entity.setPassword(dto.getTeacherPassword());
        entity.setTeacherLastName(dto.getTeacherLastName());
        entity.setTeacherFirstName(dto.getTeacherFirstName());
        entity.setActive(dto.isActive() ? Byte.valueOf("1") : Byte.valueOf("0"));
        return entity;
    }

    @Override
    public TeacherDTO entityToDTO(Teacher entity) {
        if (entity == null) return null;
        TeacherDTO dto = new TeacherDTO();
        dto.setTeacherId(entity.getId());
        dto.setTeacherLastName(entity.getTeacherLastName());
        dto.setTeacherFirstName(entity.getTeacherFirstName());
        dto.setTeacherPassword(entity.getPassword());
        dto.setActive(entity.getActive().equals(Byte.valueOf("1")));
        return dto;
    }
}
