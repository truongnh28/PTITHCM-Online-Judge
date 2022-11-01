package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.helper.TimeHelper;
import ptithcm.onlinejudge.model.entity.Teacher;

@Component
public class TeacherMapperImpl implements TeacherMapper {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public Teacher dtoToEntity(TeacherDTO dto) {
        if (dto == null) return null;
        Teacher entity = new Teacher();
        entity.setId(dto.getTeacherId());
        entity.setPassword(dto.getTeacherPassword());
        entity.setTeacherLastName(dto.getTeacherLastName());
        entity.setTeacherFirstName(dto.getTeacherFirstName());
        entity.setActive(dto.isActive() ? Byte.valueOf("1") : Byte.valueOf("0"));
        entity.setRole(roleMapper.dtoToEntity(dto.getRole()));
        if (dto.getCreateAt() != null) entity.setCreateAt(TimeHelper.convertStringToInstance(dto.getCreateAt()));
        if (dto.getUpdateAt() != null) entity.setUpdateAt(TimeHelper.convertStringToInstance(dto.getUpdateAt()));
        if (dto.getLastLogin() != null) entity.setLastLogin(TimeHelper.convertStringToInstance(dto.getLastLogin()));
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
        dto.setActive(entity.getActive() == (byte) 1);
        dto.setRole(roleMapper.entityToDTO(entity.getRole()));
        dto.setCreateAt(TimeHelper.convertInstantToString(entity.getCreateAt()));
        dto.setUpdateAt(TimeHelper.convertInstantToString(entity.getUpdateAt()));
        dto.setLastLogin(TimeHelper.convertInstantToString(entity.getLastLogin()));
        return dto;
    }
}
