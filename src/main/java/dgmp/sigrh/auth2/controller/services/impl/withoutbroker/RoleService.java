package dgmp.sigrh.auth2.controller.services.impl.withoutbroker;

import dgmp.sigrh.auth2.controller.repositories.RoleHistoRepo;
import dgmp.sigrh.auth2.controller.repositories.RoleRepo;
import dgmp.sigrh.auth2.controller.services.spec.IRoleService;
import dgmp.sigrh.auth2.model.dtos.approle.CreateRoleDTO;
import dgmp.sigrh.auth2.model.dtos.approle.ReadRoleDTO;
import dgmp.sigrh.auth2.model.dtos.approle.RoleMapper;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.events.types.auth.RoleEventTypes;
import dgmp.sigrh.auth2.model.histo.RoleHisto;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.shared.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class RoleService implements IRoleService
{
    private final RoleRepo roleRepo;
    private final RoleMapper roleMapper;
    private final ISecurityContextManager scm;
    private final RoleHistoRepo roleHistoRepo;
    @Override
    public ReadRoleDTO createRole(CreateRoleDTO dto)
    {
        AppRole role = roleMapper.mapToRole(dto);
        role = roleRepo.save(role);
        RoleHisto histo = roleMapper.mapToRoleHisto(role, RoleEventTypes.CREATE, scm.getEventActorIdFromSCM());
        roleHistoRepo.save(histo);
        return roleMapper.mapToReadRoleDTO(role);
    }

    @Override
    public Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable)
    {
        Page<AppRole> rolePage = roleRepo.searchRoles(StringUtils.stripAccentsToUpperCase(searchKey), pageable);
        List<ReadRoleDTO> readRoleDTOS = rolePage.stream().map(roleMapper::mapToReadRoleDTO).collect(Collectors.toList());
        return new PageImpl<>(readRoleDTOS, pageable, rolePage.getTotalElements());
    }
}
