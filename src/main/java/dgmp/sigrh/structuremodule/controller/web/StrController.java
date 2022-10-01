package dgmp.sigrh.structuremodule.controller.web;

import dgmp.sigrh.agentmodule.controller.services.IAgentService;
import dgmp.sigrh.agentmodule.model.enums.EtatRecrutement;
import dgmp.sigrh.auth.security.services.SecurityContextManager;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.controller.service.post.IPostService;
import dgmp.sigrh.structuremodule.controller.service.str.IStrService;
import dgmp.sigrh.structuremodule.model.dtos.post.CreatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.PostMapper;
import dgmp.sigrh.structuremodule.model.dtos.post.ReadPostDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.UpdatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.*;
import dgmp.sigrh.structuremodule.model.entities.post.PostGroup;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static dgmp.sigrh.shared.model.enums.PersistenceStatus.ACTIVE;

@Controller @RequiredArgsConstructor
public class StrController
{
    private final IStrService strService;
    private final StrRepo strRepo;
    private final PostRepo postRepo;
    private final PostMapper postMapper;
    private final PostGroupRepo pgRepo;
    private final StrMapper strMapper;
    private final SecurityContextManager scm;
    private final IAgentService agentService;
    private final IPostService postService;
    private final PostParamRepo ppRepo;

    @GetMapping(path = "/sigrh/structures")
    public String gotoStrLayout(Model model)
    {
        return "structures/str-layout";
    }

    @GetMapping(path = "/sigrh/structures/str-list")
    public String gotoStrList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "1") int navigating)
    {
        pageNum = navigating == 1 ? pageNum : 0;
        Long parentId = scm.getCurrentRoleAss() == null ? null : scm.getCurrentRoleAss().getStructure() == null ? null : scm.getCurrentRoleAss().getStructure().getStrId();
        Page<ReadStrDTO> structures = strService.searchStrByParent(key,parentId, pageNum, pageSize);
        structures.forEach(str->str.setHierarchy(strService.getParents(str.getStrId())));

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("structures", structures);
        model.addAttribute("pages", new long[structures.getTotalPages()]);
        model.addAttribute("viewMode", "list");

        return "structures/strList";
    }

    @GetMapping(path = "/sigrh/structures/add-posts-form")
    public String gotoStrAddPostsForm(Model model, @RequestParam(defaultValue = "0") long strId)
    {
        Long visibility = scm.getCurrentRoleAss() == null ? null : scm.getCurrentRoleAss().getStructure() == null ? null : scm.getCurrentRoleAss().getStructure().getStrId();
        List<Structure> structures = visibility == null ? strRepo.findByStatus(ACTIVE) : strService.getAllChildren(visibility);
        CreatePostDTO dto = new CreatePostDTO();
        dto.setStrId(strId);
        model.addAttribute("dto", dto);
        model.addAttribute("str", strRepo.findById(strId).orElse(new Structure()));
        model.addAttribute("structures", structures);
        return "structures/addPostsForm";
    }

    @GetMapping(path = "/sigrh/posts/update-post-form")
    public String gotoUpdatePostsForm(Model model, @RequestParam(defaultValue = "0") long postGroupId)
    {
        //Long visibility = scm.getCurrentRoleAss() == null ? null : scm.getCurrentRoleAss().getStructure() == null ? null : scm.getCurrentRoleAss().getStructure().getStrId();
        PostGroup pg = pgRepo.findById(postGroupId).orElse(new PostGroup());
        UpdatePostDTO dto = new UpdatePostDTO();
        dto.setPostGroupId(postGroupId);
        dto.setEmploisIds(ppRepo.getEmploiIdsByPost(postGroupId));
        dto.setIntitule(pg.getIntitule());
        dto.setPostDescription(pg.getPostDescription());
        model.addAttribute("dto", dto);
        model.addAttribute("pg", pg);
        model.addAttribute("hierarchy", strService.getParents(pg.getStructure().getStrId()));
        return "structures/updatePostsForm";
    }

    @PostMapping(path = "/sigrh/structures/update-posts")
    public String updatePosts(Model model, @Valid UpdatePostDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoUpdatePostsForm(model, dto.getPostGroupId());
        }
        postService.updatePostGroup(dto);
        return "redirect:/sigrh/posts/post-details/"+dto.getPostGroupId();
    }

    @PostMapping(path = "/sigrh/structures/add-posts")
    public String addPostesToStr(Model model, @Valid CreatePostDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoStrAddPostsForm(model, dto.getStrId());
        }
       postService.createPosts(dto);
        return "redirect:/sigrh/structures/str-details/"+dto.getStrId();
    }

    @GetMapping(path = "/sigrh/structures/str-details/{strId}")
    public String gotoStrDetails(Model model, @PathVariable Long strId)
    {
        model.addAttribute("viewMode", "details");
        ReadStrDTO str = strMapper.mapToReadStrDTO( strRepo.findById(strId).orElse(new Structure()));
        str.setHierarchy(strService.getParents(strId));
        model.addAttribute("str",str);
        model.addAttribute("agents", agentService.getAllAgentsByStr(strId, EtatRecrutement.getWithUs(true)));
        model.addAttribute("posts", postService.searchPostsByStr(strId));

        return "structures/strDetails";
    }

    @GetMapping(path = "/sigrh/posts/post-details/{postGroupId}")
    public String gotoPostDetails(Model model, @PathVariable Long postGroupId)
    {
        model.addAttribute("viewMode", "details");
        PostGroup postGroup = pgRepo.findById(postGroupId).orElse(null);
        ReadPostDTO pg = postMapper.mapToReadPostDTO(postGroup == null ? new PostGroup() : postGroup);
        pg.setHierarchy(strService.getParents(postGroup.getStructure().getStrId()));
        model.addAttribute( "pg",pg);
        model.addAttribute("noneVacantPosts", postRepo.findNoneVacantPostsByPostGroup(postGroupId));
        return "structures/postDetails";
    }

    @GetMapping(path = "/sigrh/posts/remove-compatibles-emplois")
    public String removeCompatibleEmploi(@RequestParam long postGroupId, @RequestParam long emploiId)
    {
        postService.removeCompatibleEmp(postGroupId, emploiId);
        return "redirect:/sigrh/posts/post-details/" + postGroupId;
    }

    @GetMapping(path = "/sigrh/structures/new-str-form")
    public String gotoStrForm(Model model)
    {
        model.addAttribute("str", new CreateStrDTO());
        model.addAttribute("viewMode", "new");
        return "structures/newStrForm";
    }

    @GetMapping(path = "/sigrh/structures/update-str-form/{strId}")
    public String gotoStrUpdateForm(Model model, @PathVariable long strId)
    {
        UpdateStrDTO dto = strMapper.mapToUpdateStrDTO(strRepo.findById(strId).orElse(new Structure()));

        model.addAttribute("str", dto);
        model.addAttribute("viewMode", "update");
        return "structures/updateStrForm";
    }

    @PostMapping(path = "/sigrh/structures/str/create")
    public String createStr(RedirectAttributes ra, Model model, @Valid CreateStrDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoStrForm(model);
        }
        strService.createStr(dto);
        return "redirect:/sigrh/structures/new-str-form";
    }

    @PostMapping(path = "/sigrh/structures/str/update")
    public String updateStr(RedirectAttributes ra, Model model, @Valid UpdateStrDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoStrUpdateForm(model, dto.getStrId());
        }
        strService.updateStr(dto);
        return "redirect:/sigrh/structures/str-details/"+dto.getStrId();
    }

    @GetMapping(path = "/sigrh/structures/child-type/{childTypeId}") @ResponseBody
    public List<ReadStrDTO> getStrByChildType(@PathVariable Long childTypeId)
    {
        List<Structure> strs = strRepo.findByChildType(childTypeId);
        return strs.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
    }

    @GetMapping(path = "/sigrh/structures/loadStrTreeView/{strId}") @ResponseBody
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId)
    {
        return this.strService.loadStrTreeView(strId);
    }

    @GetMapping(path = "/sigrh/structures/loadStrTreeView/{strId}/{critere}") @ResponseBody
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId, @PathVariable String critere)
    {
        return this.strService.loadStrTreeView(strId, critere);
    }

    @GetMapping(path = "/sigrh/structures/countAllChildren/{strId}") @ResponseBody
    public long countAllChildren(@PathVariable Long strId)
    {
        return this.strService.countAllChildren(strId);
    }
}
