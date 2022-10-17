package dgmp.sigrh.structuremodule.controller.service.post;

import dgmp.sigrh.structuremodule.model.dtos.post.CreatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.ReadPostDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.UpdatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface IPostService
{
    ReadPostDTO createPosts(CreatePostDTO dto);
    ReadPostDTO updatePostGroup(UpdatePostDTO dto);
    ReadPostDTO deletePost(Long postId);
    ReadPostDTO restorePost(Long postId);
    void removeCompatibleEmp(long pgId, long empId);

    Page<ReadPostDTO> searchPosts(String key, int pageNum, int pageSize);
    Page<ReadPostDTO> searchPostsWithEmplois(String key, Set<Long> emploiIds, int pageNum, int pageSize);

    Page<ReadPostDTO> searchPostsInEmplois(String key, Set<Long> emploiIds, int pageNum, int pageSize);

    Page<ReadPostDTO> searchPostsByStr(long strId, String key, int pageNum, int pageSize);

    Page<ReadStrDTO> searchPostsByParent(String key, Long strId, int pageNum, int pageSize);

    Page<ReadPostDTO> searchPostsByStr(Long strId, String key, int pageNum, int pageSize);
    List<ReadPostDTO> searchPostsByStrWithEmplois(String key, Long strId, Set<Long> emploiIds);

    List<ReadPostDTO> searchPostsByStrInEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize);
    /*
    List<ReadPostDTO> searchVacantPostsByStrWithEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize);

    List<ReadPostDTO> searchVacantPostsByStrInEmplois(String key, Long strId, Set<Long> emploiIds, int pageNum, int pageSize);*/
}
