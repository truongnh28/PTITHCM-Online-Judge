package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.StudentDTO;
import ptithcm.onlinejudge.dto.StudentShowDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
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
        entity.setStudentClass(dto.getStudentClass());
        entity.setPassword(dto.getStudentPassword());
        entity.setActive(dto.getActive());
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getLastLogin() != null) entity.setLastLogin(TimeHelper.convertStringToInstance(dto.getLastLogin()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        return entity;
    }

    @Override
    public StudentDTO entityToDTO(Student entity) {
        if (entity == null) return null;
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(entity.getId());
        dto.setActive(entity.getActive());
        dto.setStudentFirstName(entity.getStudentFirstName());
        dto.setStudentLastName(entity.getStudentLastName());
        dto.setStudentClass(entity.getStudentClass());
        dto.setStudentPassword(entity.getPassword());
        if (entity.getCreateAt() != null) dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getLastLogin() != null) dto.setLastLogin(TimeHelper.convertInstantToString(entity.getLastLogin()));
        return dto;
    }

    @Override
    public StudentShowDTO entityToShowDTO(Student entity) {
        if (entity == null) return null;
        StudentShowDTO showDTO = new StudentShowDTO();
        showDTO.setStudentId(entity.getId());
        showDTO.setActive(entity.getActive());
        showDTO.setStudentFirstName(entity.getStudentFirstName());
        showDTO.setStudentLastName(entity.getStudentLastName());
        showDTO.setStudentClass(entity.getStudentClass());
        showDTO.setStudentPassword(entity.getPassword());
        if (entity.getCreateAt() != null) showDTO.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        if (entity.getUpdateAt() != null) showDTO.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        if (entity.getLastLogin() != null) showDTO.setLastLogin(TimeHelper.convertInstantToString(entity.getLastLogin()));
        return showDTO;
    }
}
