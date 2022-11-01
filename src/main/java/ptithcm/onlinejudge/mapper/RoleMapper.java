package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.RoleDTO;
import ptithcm.onlinejudge.model.entity.Role;

public interface RoleMapper {
    Role dtoToEntity(RoleDTO dto);
    RoleDTO entityToDTO(Role entity);
}
