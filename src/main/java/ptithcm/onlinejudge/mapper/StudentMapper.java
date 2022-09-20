package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.StudentShowDTO;
import ptithcm.onlinejudge.model.entity.Student;

public interface StudentMapper {
    Student dtoToEntity(StudentDTO dto);
    StudentDTO entityToDTO(Student entity);

    StudentShowDTO entityToShowDTO(Student entity);
}
