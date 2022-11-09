package dgmp.sigrh.structuremodule.controller.service.post;

import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamHistoRepo;
import dgmp.sigrh.structuremodule.model.dtos.post.PostMapper;
import dgmp.sigrh.structuremodule.model.entities.post.PostParam;
import dgmp.sigrh.structuremodule.model.entities.post.PostParamHisto;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class PostParamHistoService implements IHistoService<PostParam, PostParamHisto, PostEventType>
{
    private final PostParamHistoRepo postParamHistoRepo;
    private final PostMapper postMapper;
    private final ISecurityContextManager scm;
    @Override
    public PostParamHisto storeEntity(PostParam postParam, PostEventType eventType)
    {
        PostParamHisto pph = postMapper.mapToPostParamHisto(postParam, eventType, scm.getEventActorIdFromSCM());
        return postParamHistoRepo.save(pph);
    }

    @Override
    public PostParamHisto storeEntity(PostParam postParam, PostEventType eventType, String actionId, String mainActionName)
    {
        PostParamHisto pph = postMapper.mapToPostParamHisto(postParam, eventType, scm.getEventActorIdFromSCM(), actionId, mainActionName);
        return postParamHistoRepo.save(pph);
    }

    @Override
    public Page<PostParamHisto> getHistoPageBetweenPeriod(Long entityId, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<PostParamHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<PostParamHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }
}
