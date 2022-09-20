package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.model.entity.Teacher;

public interface TeacherMapper {
    Teacher dtoToEntity(TeacherDTO dto);
    TeacherDTO entityToDTO(Teacher entity);
}
