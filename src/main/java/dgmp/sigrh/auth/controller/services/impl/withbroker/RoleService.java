package dgmp.sigrh.auth.controller.services.impl.withbroker;

import dgmp.sigrh.auth.controller.repositories.RoleDAO;
import dgmp.sigrh.auth.controller.repositories.RoleToUserAssRepo;
import dgmp.sigrh.auth.controller.services.spec.IRoleService;
import dgmp.sigrh.auth.model.dtos.approle.CreateRoleDTO;
import dgmp.sigrh.auth.model.dtos.approle.ReadRoleDTO;
import dgmp.sigrh.auth.model.dtos.approle.RoleMapper;
import dgmp.sigrh.auth.model.entities.AppRole;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import dgmp.sigrh.auth.model.events.types.auth.RoleEventTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static dgmp.sigrh.auth.security.SecurityConstants.AUTH_TOPIC;

@Service
@RequiredArgsConstructor @Slf4j
@Profile("withbroker")
public class RoleService implements IRoleService
{
    private final RoleDAO roleDAO;
    private final RoleToUserAssRepo rtuRepo;
    private final RoleMapper roleMapper;
    private final BrokerMessageSender brokerMessageSender;
    @Override
    public ReadRoleDTO createRole(CreateRoleDTO dto)
    {
        AppRole role = roleMapper.getAppRole(dto);
        role = roleDAO.save(role);
        brokerMessageSender.sendEvent(AUTH_TOPIC, RoleEventTypes.CREATE, role);
        return roleMapper.getReadRoleDTO(role);
    }

    @Override
    public Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable)
    {
        Page<AppRole> rolePage = roleDAO.searchRoles(searchKey, pageable);
        List<ReadRoleDTO> readRoleDTOS = rolePage.stream().map(roleMapper::getReadRoleDTO).collect(Collectors.toList());
        return new PageImpl<>(readRoleDTOS, pageable, rolePage.getTotalElements());
    }

    @Override
    public RoleToUserAss getUserActiveRoleAss(Long userId)
    {
        List<RoleToUserAss> roleToUserAsses = rtuRepo.getUsersActiveRoleAss(userId);
        return roleToUserAsses.size() != 1 ? null : roleToUserAsses.get(0);
    }


}
