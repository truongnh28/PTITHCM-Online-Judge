package ptithcm.onlinejudge.mapper;

import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.RoleDTO;
import ptithcm.onlinejudge.model.entity.Role;

@Component
public class RoleMapperImpl implements RoleMapper {
    @Override
    public Role dtoToEntity(RoleDTO dto) {
        if (dto == null) return null;
        return new Role((byte) dto.getRoleId(), dto.getRoleName());
    }

    @Override
    public RoleDTO entityToDTO(Role entity) {
        if (entity == null) return null;
        return new RoleDTO(entity.getId(), entity.getRoleName());
    }
}
