package dgmp.sigrh.structuremodule.controller.service.post;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.shared.controller.exception.AppException;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.controller.service.str.IStrService;
import dgmp.sigrh.structuremodule.model.dtos.post.*;
import dgmp.sigrh.structuremodule.model.entities.post.*;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class PostService implements IPostService
{
    private final PostRepo postRepo;
    private final PostGroupRepo pgRepo;
    private final PostMapper postMapper;
    private final IStrService strService;
    private final PostParamRepo postParamRepo;
    private final IHistoService<Post, PostHisto, PostEventType> postHistoService;
    private final IHistoService<PostParam, PostParamHisto, PostEventType> postParamHistoService;
    private final IHistoService<PostGroup, PostGroupHisto, PostEventType> postGroupHistoService;

    @Override @Transactional
    public ReadPostDTO createPosts(CreatePostDTO dto)
    {
        String actionId = UUID.randomUUID().toString();
        String mainAction = PostEventType.CREATE_POSTS.name();
        PostGroup postGroup = pgRepo.save(postMapper.mapToPostGroup(dto));
        PostGroupHisto pgh = postGroupHistoService.storeEntity(postGroup, PostEventType.CREATE_POST_GROUP, actionId, mainAction);
        for(int i = 0; i< dto.getNbrPosts(); i++)
        {
            Post post = postMapper.mapToPost(postGroup);
            post = postRepo.save(post);
            PostHisto ph = postHistoService.storeEntity(post, PostEventType.CREATE_POST, actionId, mainAction);
            ph.getEai().setActionId(actionId);
            ph.getEai().setMainActionName(mainAction);
        }
        dto.getEmploisIds().forEach(id->
        {
            PostParam postParam = postParamRepo.save(new PostParam(postGroup.getPostGroupId(), id, PersistenceStatus.ACTIVE));
            PostParamHisto pph = postParamHistoService.storeEntity(postParam, PostEventType.ADD_EMPLOI_TO_POST, actionId, mainAction);
        });
        return postMapper.mapToReadPostDTO(postGroup);
    }

    @Override @Transactional
    public ReadPostDTO updatePostGroup(UpdatePostDTO dto)
    {
        String actionId = UUID.randomUUID().toString();
        String mainAction = PostEventType.UPDATE_POST_GROUP.name();
        PostGroup postGroup = postMapper.mapToPostGroup(dto);
        postGroup = pgRepo.save(postGroup);
        PostGroupHisto pgh = postGroupHistoService.storeEntity(postGroup, PostEventType.UPDATE_POST_GROUP, actionId, mainAction);

        SetPostParamsDTO setPostParamsDTO = postMapper.mapTSetPostParamsDTO(dto.getPostGroupId(), dto.getEmploisIds());
        setPostParamsDTO.getRemovableEmpIds().forEach(id->
        {
            PostParam pp = postParamRepo.findByPostAndEmploi(dto.getPostGroupId(), id);
            pp.setStatus(PersistenceStatus.DELETED);
            PostParamHisto pph = postParamHistoService.storeEntity(pp, PostEventType.REMOVE_EMPLOI_TO_POST, actionId, mainAction);
        });

        setPostParamsDTO.getNewEmpIdsToBeAdded().forEach(id->
        {
            if(postParamRepo.existsByPostAndEmploi(dto.getPostGroupId(), id))
            {
                PostParam pp = postParamRepo.findByPostAndEmploi(dto.getPostGroupId(), id);
                pp.setStatus(PersistenceStatus.ACTIVE);
                PostParamHisto pph = postParamHistoService.storeEntity(pp, PostEventType.RESTORE_EMPLOI_TO_POST, actionId, mainAction);
            }
            else
            {
                PostParam pp = new PostParam(dto.getPostGroupId(), id, PersistenceStatus.ACTIVE);
                PostParamHisto pph = postParamHistoService.storeEntity(pp, PostEventType.ADD_EMPLOI_TO_POST, actionId, mainAction);
            }
        });
        return postMapper.mapToReadPostDTO(postGroup);
    }

    @Override
    public ReadPostDTO deletePost(Long postId)
    {
        if(postId == null) return null;
        Post post = postRepo.findById(postId).orElse(null);
        if(post == null) return null;
        if(!post.isVacant()) throw new AppException("Impossible de supprimer un poste occupé");
        post.setStatus(PersistenceStatus.DELETED);
        PostHisto ph = postHistoService.storeEntity(post, PostEventType.DELETE_POST);
        return postMapper.mapToReadPostDTO(post);
    }

    @Override
    public ReadPostDTO restorePost(Long postId)
    {
        if(postId == null) return null;
        Post post = postRepo.findById(postId).orElse(null);
        if(post == null) return null;
        if(post.getStatus() == PersistenceStatus.ACTIVE) return postMapper.mapToReadPostDTO(post);
        post.setStatus(PersistenceStatus.ACTIVE);
        PostHisto ph = postHistoService.storeEntity(post, PostEventType.RESTORE_POST);
        return postMapper.mapToReadPostDTO(post);
    }

    @Override
    public Page<ReadPostDTO> searchPosts(String key, int pageNum, int pageSize)
    {
        Page<PostGroup> postPage = postRepo.searchPosts(key, PageRequest.of(pageNum, pageSize));
        List<ReadPostDTO> posts = postPage.stream().map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
        return new PageImpl<>(posts, PageRequest.of(pageNum, pageSize), postRepo.countPosts(key));
    }

    @Override
    public Page<ReadPostDTO> searchPostsWithEmplois(String key, Set<Long> emploiIds, int pageNum, int pageSize)
    {
        Page<PostGroup> postPage = postRepo.searchPostsWithEmplois(key, emploiIds, PageRequest.of(pageNum, pageSize));
        List<ReadPostDTO> posts = postPage.stream().map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
        return new PageImpl<>(posts, PageRequest.of(pageNum, pageSize), postRepo.countPostsWithEmplois(key, emploiIds));
    }

    @Override
    public Page<ReadPostDTO> searchPostsInEmplois(String key, Set<Long> emploiIds, int pageNum, int pageSize)
    {
        Page<PostGroup> postPage = postRepo.searchPostsInEmplois(key, emploiIds, PageRequest.of(pageNum, pageSize));
        List<ReadPostDTO> posts = postPage.stream().map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
        return new PageImpl<>(posts, PageRequest.of(pageNum, pageSize), postRepo.countPostsInEmplois(key, emploiIds));
    }

    @Override
    public List<ReadPostDTO> searchPostsByStr(String key, Long strId, int pageNum, int pageSize)
    {
        List<PostGroup> postGroups = postRepo.searchPosts(key);
        return postGroups.stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadPostDTO> searchPostsByStrWithEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchPostsWithEmplois(key, emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadPostDTO> searchPostsByStrInEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchPostsInEmplois(key, emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadPostDTO> searchVacantPostsByStrWithEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchVacantPostsWithEmplois(key, emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadPostDTO> searchVacantPostsByStrInEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchVacantPostsInEmplois(key, emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }
}