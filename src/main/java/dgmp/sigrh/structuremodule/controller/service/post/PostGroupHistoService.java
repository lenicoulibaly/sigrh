package dgmp.sigrh.structuremodule.controller.service.post;

import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupHistoRepo;
import dgmp.sigrh.structuremodule.model.dtos.post.PostMapper;
import dgmp.sigrh.structuremodule.model.entities.post.PostGroup;
import dgmp.sigrh.structuremodule.model.entities.post.PostGroupHisto;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class PostGroupHistoService implements IHistoService<PostGroup, PostGroupHisto, PostEventType>
{
    private final PostGroupHistoRepo pghRepo;
    private final ISecurityContextManager scm;
    private final PostMapper postMapper;
    @Override
    public PostGroupHisto storeEntity(PostGroup postGroup, PostEventType eventType)
    {
        PostGroupHisto postGroupHisto = postMapper.mapToPostGroupHisto(postGroup, eventType, scm.getEventActorIdFromSCM());
        return pghRepo.save(postGroupHisto);
    }

    @Override
    public PostGroupHisto storeEntity(PostGroup postGroup, PostEventType eventType, String actionId, String mainActionName) {
        PostGroupHisto postGroupHisto = postMapper.mapToPostGroupHisto(postGroup, eventType, scm.getEventActorIdFromSCM(), actionId, mainActionName);
        return pghRepo.save(postGroupHisto);
    }

    @Override
    public Page<PostGroupHisto> getHistoPageBetweenPeriod(Long entityId, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<PostGroupHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<PostGroupHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }
}
