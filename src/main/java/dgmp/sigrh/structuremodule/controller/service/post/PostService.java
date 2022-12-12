package dgmp.sigrh.structuremodule.controller.service.post;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiRepo;
import dgmp.sigrh.shared.controller.exception.AppException;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.controller.service.str.IStrService;
import dgmp.sigrh.structuremodule.model.dtos.post.*;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.entities.post.*;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE;

@Service @RequiredArgsConstructor
public class PostService implements IPostService
{
    private final PostRepo postRepo;
    private final StrRepo strRepo;
    private final PostGroupRepo pgRepo;
    private final EmploiRepo empRepo;
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
            PostParam postParam = postParamRepo.save(new PostParam(postGroup.getPostGroupId(), id, ACTIVE));
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
            PostParam pp = postParamRepo.findByPostAndEmploi(dto.getPostGroupId(), id, ACTIVE);
            if(pp != null)
            {
                pp.setStatus(PersistenceStatus.DELETED);
                PostParamHisto pph = postParamHistoService.storeEntity(pp, PostEventType.REMOVE_EMPLOI_TO_POST, actionId, mainAction);
            }
        });

        setPostParamsDTO.getNewEmpIdsToBeAdded().forEach(id->
        {
            if(postParamRepo.existsByPostAndEmploi(dto.getPostGroupId(), id))
            {
                PostParam pp = postParamRepo.findByPostAndEmploi(dto.getPostGroupId(), id);
                pp.setStatus(ACTIVE);
                PostParamHisto pph = postParamHistoService.storeEntity(pp, PostEventType.RESTORE_EMPLOI_TO_POST, actionId, mainAction);
            }
            else
            {
                PostParam pp = new PostParam(dto.getPostGroupId(), id, ACTIVE);
                pp = postParamRepo.save(pp);
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
        if(post.getStatus() == ACTIVE) return postMapper.mapToReadPostDTO(post);
        post.setStatus(ACTIVE);
        PostHisto ph = postHistoService.storeEntity(post, PostEventType.RESTORE_POST);
        return postMapper.mapToReadPostDTO(post);
    }

    @Override @Transactional
    public void removeCompatibleEmp(long pgId, long empId)
    {
        if(!pgRepo.existsById(pgId) || !empRepo.existsById(empId)) return;
        PostParam pp = postParamRepo.findByPostAndEmploi(pgId, empId, ACTIVE);
        if(pp == null) return;
        pp.setStatus(PersistenceStatus.DELETED);
        PostParamHisto pph = postParamHistoService.storeEntity(pp, PostEventType.REMOVE_EMPLOI_TO_POST);
    }

    @Override
    public Page<ReadPostDTO> searchPosts(String key, int pageNum, int pageSize)
    {
        Page<PostGroup> postPage = postRepo.searchPostGroups(key, PageRequest.of(pageNum, pageSize));
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
    public Page<ReadPostDTO> searchPostsByStr(long strId, String key, int pageNum, int pageSize)
    {
        Page<PostGroup> postGroups = postRepo.searchPostGroups(strId, key, PageRequest.of(pageNum, pageSize));
        List<ReadPostDTO> dtos = postGroups.stream().map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
        long nbStr = postRepo.countPosts(strId, key);
        return new PageImpl<>(dtos, PageRequest.of(pageNum, pageSize), nbStr);
    }

    @Override
    public Page<ReadStrDTO> searchPostsByParent(String key, Long strId, int pageNum, int pageSize)
    {
        if(strId!= null )
        {
            if (!strRepo.existsById(strId))
                return new PageImpl<>(new ArrayList<>(), PageRequest.of(pageNum, pageSize), 0);
        }
        Page<Structure> strPage = strRepo.searchStr(key.trim(), ACTIVE, PageRequest.of(pageNum, pageSize));

        //List<ReadStrDTO> strDTOS = strId == null ?
        //        strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList()) :
        //        strPage.stream().filter(str->this.childBelongToParent(str.getStrId(), strId)).map(strMapper::mapToReadStrDTO).collect(Collectors.toList());

        return null; //new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), key==null || "".equals(key) ? countAllChildren(strId) : countAllChildren(strId, key));
    }

    @Override
    public Page<ReadPostDTO> searchPostsByStr(Long strId, String key, int pageNum, int pageSize)
    {
        if(strId == null) return new PageImpl<>(new ArrayList<>());
        if(!strRepo.existsById(strId)) return new PageImpl<>(new ArrayList<>());
        Page<PostGroup> postGroups = key==null ? postRepo.findPostGroupByStr(strId, PageRequest.of(pageNum, pageSize)) :
                key.trim().equals("") ? postRepo.findPostGroupByStr(strId, PageRequest.of(pageNum, pageSize)) :
                postRepo.searchPostGroups(strId, StringUtils.stripAccentsToUpperCase(key), PageRequest.of(pageNum, pageSize));

        List<ReadPostDTO> readPostDTOS = postGroups.stream().map(pg->{
            ReadPostDTO dto = postMapper.mapToReadPostDTO(pg);
            dto.setHierarchy(strService.getParents(dto.getStrId()));
            return dto;
        }).collect(Collectors.toList());
        return new PageImpl<>(readPostDTOS, PageRequest.of(pageNum, pageSize), postGroups.getTotalElements());
    }

    @Override
    public List<ReadPostDTO> searchPostsByStrWithEmplois(String key, Long strId, Set<Long> emploiIds) {
        return postRepo.searchPostsWithEmplois(StringUtils.stripAccentsToUpperCase(key), emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadPostDTO> searchPostsByStrInEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchPostsInEmplois(StringUtils.stripAccentsToUpperCase(key), emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }
/*
    @Override
    public List<ReadPostDTO> searchVacantPostsByStrWithEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchVacantPostsWithEmplois(key, emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReadPostDTO> searchVacantPostsByStrInEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize) {
        return postRepo.searchVacantPostsInEmplois(key, emploiIds).stream().filter(pg->strService.childBelongToParent(pg.getStructure().getStrId(), strId)).map(postMapper::mapToReadPostDTO).collect(Collectors.toList());
    }*/
}