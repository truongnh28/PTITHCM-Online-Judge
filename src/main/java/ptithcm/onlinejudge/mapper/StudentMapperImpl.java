package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.StudentShowDTO;
import ptithcm.onlinejudge.model.entity.Student;

import java.util.Objects;

@Component
public class StudentMapperImpl implements StudentMapper{
    @Override
    public Student dtoToEntity(StudentDTO dto) {
        if (dto == null) return null;
        Student entity = new Student();
        entity.setId(dto.getStudentId());
        entity.setStudentFirstName(dto.getStudentFirstName());
        entity.setStudentLastName(dto.getStudentLastName());
        entity.setPassword(dto.getStudentPassword());
        entity.setActive(Byte.valueOf(dto.isActive() ? "1": "0"));
        return entity;
    }

    @Override
    public StudentDTO entityToDTO(Student entity) {
        if (entity == null) return null;
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(entity.getId());
        dto.setActive(Objects.equals(entity.getActive(), Byte.valueOf("1")));
        dto.setStudentFirstName(entity.getStudentFirstName());
        dto.setStudentLastName(entity.getStudentLastName());
        dto.setStudentPassword(entity.getPassword());
        return dto;
    }

    @Override
    public StudentShowDTO entityToShowDTO(Student entity) {
        if (entity == null) return null;
        StudentShowDTO showDTO = new StudentShowDTO();
        showDTO.setStudentId(entity.getId());
        showDTO.setActive(Objects.equals(entity.getActive(), Byte.valueOf("1")));
        showDTO.setStudentFirstName(entity.getStudentFirstName());
        showDTO.setStudentLastName(entity.getStudentLastName());
        showDTO.setStudentPassword(entity.getPassword());
        return showDTO;
    }
}
