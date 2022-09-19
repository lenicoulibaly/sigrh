package dgmp.sigrh.structuremodule.controller.service.post;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostHistoRepo;
import dgmp.sigrh.structuremodule.model.dtos.post.PostMapper;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.structuremodule.model.entities.post.PostHisto;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class PostHistoService implements IHistoService<Post, PostHisto, PostEventType>
{
    private final PostHistoRepo postHistoRepo;
    private final PostMapper postMapper;
    private final ISecurityContextManager scm;

    @Override
    public PostHisto storeEntity(Post post, PostEventType eventType)
    {
        PostHisto postHisto = postMapper.mapToPostHisto(post, eventType, scm.getEventActorIdFromSCM());
        return postHistoRepo.save(postHisto);
    }


    @Override
    public PostHisto storeEntity(Post post, PostEventType eventType, String actionId, String mainActionName)
    {
        PostHisto postHisto = postMapper.mapToPostHisto(post, eventType, scm.getEventActorIdFromSCM(), actionId, mainActionName);
        return postHistoRepo.save(postHisto);
    }

    @Override
    public Page<PostHisto> getHistoPageBetweenPeriod(Long entityId, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<PostHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<PostHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return null;
    }
}
