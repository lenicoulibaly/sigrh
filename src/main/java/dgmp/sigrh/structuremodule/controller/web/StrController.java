package dgmp.sigrh.structuremodule.controller.web;

import dgmp.sigrh.agentmodule.controller.services.IAgentService;
import dgmp.sigrh.agentmodule.model.dtos.ReadAgentDTO;
import dgmp.sigrh.agentmodule.model.enums.EtatRecrutement;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.security.services.SecurityContextManager;
import dgmp.sigrh.shared.utilities.DateParser;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostGroupRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostParamRepo;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrHistoRepo;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.controller.service.post.IPostService;
import dgmp.sigrh.structuremodule.controller.service.str.IStrService;
import dgmp.sigrh.structuremodule.model.dtos.post.CreatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.PostMapper;
import dgmp.sigrh.structuremodule.model.dtos.post.ReadPostDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.UpdatePostDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.*;
import dgmp.sigrh.structuremodule.model.entities.post.PostGroup;
import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private final StrHistoRepo strHistoRepo;
    private final UserRepo userRepo;

    @GetMapping(path = "/sigrh/structures")
    public String gotoStrLayout(Model model)
    {
        return "structures/str-layout";
    }

    @GetMapping(path = "/sigrh/structures/str-list")
    public String gotoStrList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "1") int navigating)
    {
        pageNum = navigating == 1 ? pageNum : 0;
        Long parentId = scm.getVisibilityId();
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
        Long visibility = scm.getUserVisibilityId();
        List<Structure> structures = visibility == null ? strRepo.findByStatus(ACTIVE) : strRepo.findAllChildren(visibility);
        List<ReadStrDTO> dtos = structures.stream().map(s->{
            ReadStrDTO dto = strMapper.mapToReadStrDTO(s);
            dto.setHierarchy(strService.getParents(s.getStrId()));
            return dto;
        }).collect(Collectors.toList());
        dtos.forEach(s-> s.setStrName(s.getStrName() + "(" + s.getHierarchy().stream().map(Structure::getStrSigle).reduce("", (sg1, sg2)->sg1 + "/" + sg2).substring(1) +")"));
        CreatePostDTO dto = new CreatePostDTO();
        dto.setStrId(strId);
        model.addAttribute("dto", dto);
        model.addAttribute("str", strRepo.findById(strId).orElse(new Structure()));
        model.addAttribute("structures", dtos);
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
    public String updatePosts(Model model, RedirectAttributes ra, @Valid UpdatePostDTO dto, BindingResult br)
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
    public String addPostesToStr(Model model, RedirectAttributes ra, @Valid CreatePostDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoStrAddPostsForm(model, dto.getStrId());
        }
        postService.createPosts(dto);
        ra.addAttribute("strId", dto.getStrId());
        ra.addAttribute("tab", "str-posts-list");
        return "redirect:/sigrh/structures/str-details?strId="+dto.getStrId();
    }

    @GetMapping(path = "/sigrh/structures/str-details")
    public String gotoStrDetails(Model model, @RequestParam(defaultValue = "0") Long strId, @RequestParam(defaultValue = "str-details")String tab
            , @RequestParam(defaultValue = "") String persKey, @RequestParam(defaultValue = "0") int persPageNum, @RequestParam(defaultValue = "10") int persPageSize
            , @RequestParam(defaultValue = "") String pgKey, @RequestParam(defaultValue = "0") int pgPageNum, @RequestParam(defaultValue = "10") int pgPageSize
            , @RequestParam(required = false) String modifierUsername, @RequestParam(required = false) ArrayList<String> eventTypes, @RequestParam(defaultValue = "0") int strHistoPageNum, @RequestParam(defaultValue = "10") int strHistoPageSize,
              @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") String before,
              @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusYears(2)}") String after)
    {

        Page<ReadAgentDTO> persPages = agentService.searchAgentByStrAndEtat(strId, EtatRecrutement.getWithUs(true), persKey, PageRequest.of(persPageNum, persPageSize));
        model.addAttribute("persPageNum", persPageNum);
        model.addAttribute("persCurrentPage", persPageNum);
        model.addAttribute("persPageSize", persPageSize);
        model.addAttribute("persKey", persKey);
        model.addAttribute("persPages", new long[persPages.getTotalPages()]);
        model.addAttribute("agents", persPages);

        Page<ReadPostDTO> pgPages = postService.searchPostsByStr(strId, pgKey, pgPageNum, pgPageSize);
        model.addAttribute("pgPageNum", pgPageNum);
        model.addAttribute("pgCurrentPage", pgPageNum);
        model.addAttribute("pgPageSize", pgPageSize);
        model.addAttribute("pgKey", pgKey);
        model.addAttribute("pgPages", new long[pgPages.getTotalPages()]);
        model.addAttribute("posts", pgPages);

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter beforeFormatter = DateTimeFormatter.ofPattern(DateParser.determineDateFormat(before));
        DateTimeFormatter afterFormatter = DateTimeFormatter.ofPattern(DateParser.determineDateFormat(after));
        modifierUsername = modifierUsername == null ? null : modifierUsername.trim().equals("") ? null : modifierUsername;
        Page<StrHisto> strHistoPages = strHistoRepo.getHistoPageBetweenPeriod(strId, modifierUsername, eventTypes == null ? StrEventType.getAll() : eventTypes.isEmpty() ? StrEventType.getAll() : eventTypes.stream().map(StrEventType::getEnum).collect(Collectors.toList()), LocalDate.parse(before, beforeFormatter), LocalDate.parse(after, afterFormatter),  PageRequest.of(strHistoPageNum, strHistoPageSize));
        strHistoPages.forEach(histo->histo.setHierarchy(strService.getHistoParents(strId, histo.getEai().getModificationDate())));
        model.addAttribute("strHistoPageNum", strHistoPageNum);
        model.addAttribute("strHistoCurrentPage", strHistoPageNum);
        model.addAttribute("strHistoPageSize", strHistoPageSize);
        model.addAttribute("modifierUsername", modifierUsername);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("before", before);
        model.addAttribute("after", after);
        model.addAttribute("strHistoPages", new long[strHistoPages.getTotalPages()]);
        model.addAttribute("modificationList", strHistoPages);
        Long visibility = scm.getVisibilityId();
        model.addAttribute("users", visibility == null ? userRepo.findAll() : userRepo.findUserUnderStr(visibility, PageRequest.of(strHistoPageNum, strHistoPageSize)));
        model.addAttribute("eventTypesList", StrEventType.getAll());

        model.addAttribute("viewMode", "details");
        ReadStrDTO str = strMapper.mapToReadStrDTO(strRepo.findById(strId).orElse(new Structure()));
        str.setStrId(strId);
        str.setHierarchy(strService.getParents(strId));
        model.addAttribute("str",str);

        model.addAttribute("tab", tab);



        return "structures/strDetails";
    }

    @GetMapping(path = "/sigrh/posts/post-details/{postGroupId}")
    public String gotoPostDetails(Model model, @PathVariable Long postGroupId)
    {
        model.addAttribute("viewMode", "details");
        PostGroup postGroup = pgRepo.findById(postGroupId).orElse(null);
        ReadPostDTO pg = postMapper.mapToReadPostDTO(postGroup == null ? new PostGroup() : postGroup);
        pg.setHierarchy(postGroup == null ? new ArrayList<>() : strService.getParents(postGroup.getStructure().getStrId()));
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


    @GetMapping(path = "/sigrh/structures/change-anchor-form")
    public String gotoChangeAnchorForm(Model model, @RequestParam long strId)
    {
        ReadStrDTO str = new ReadStrDTO();
        str.setStrId(strId);
        str.setHierarchySigles(strService.getHierarchySigles(strId));
        str.setStrName(strRepo.getStrName(strId));
        ChangeAnchorDTO dto = strRepo.getChangeAnchorDTO(strId);
        Long visibility = scm.getVisibilityId();

        model.addAttribute("str", str);
        model.addAttribute("dto", dto != null ? dto : new ChangeAnchorDTO());
        model.addAttribute("parents", visibility == null ? strRepo.findPossibleParents(strId).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList()) : strRepo.findPossibleParents(strId, visibility).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList()));
        model.addAttribute("viewMode", "change-anchor");
        return "structures/changeAnchorForm";
    }


    @PostMapping(path = "/sigrh/structures/str/changeAnchor")
    public String ChangeAnchor(RedirectAttributes ra, Model model, @Valid ChangeAnchorDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoStrUpdateForm(model, dto.getStrId());
        }
        strService.changeAncrage(dto);
        ra.addAttribute("strId", dto.getStrId());
        return "redirect:/sigrh/structures/str-details";
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
        ra.addAttribute("strId", dto.getStrId());
        return "redirect:/sigrh/structures/str-details";
    }

    @GetMapping(path = "/sigrh/structures/child-type/{childTypeId}") @ResponseBody
    public List<ReadStrDTO> getStrByChildType(@PathVariable Long childTypeId)
    {
        Long visibility = scm.getVisibilityId();
        List<Structure> strs = visibility == null ? strRepo.findByChildType(childTypeId) : strRepo.findByChildType(childTypeId, visibility);
        return strs.stream().map(str->{ReadStrDTO dto = strMapper.mapToReadStrDTO(str);
            dto.setStrName(str.getStrName() + "(" +strService.getParents(str.getStrId()).stream().map(Structure::getStrSigle).reduce("",(s1, s2)->s1+"/"+s2).substring(1) + ")");
        return dto;}).collect(Collectors.toList());
    }

    @GetMapping(path = "/sigrh/structures/loadStrTreeView/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId)
    {
        return this.strService.loadStrTreeView(strId);
    }

    @GetMapping(path = "/sigrh/structures/loadStrTreeView/{strId}/{critere}") @ResponseBody @PreAuthorize("permitAll()")
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId, @PathVariable String critere)
    {
        return this.strService.loadStrTreeView(strId, critere);
    }

    @GetMapping(path = "/sigrh/structures/countAllChildren/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public long countAllChildren(@PathVariable Long strId)
    {
        return this.strRepo.countAllChildren(strId);
    }

    @GetMapping(path = "/sigrh/structures/getChildrenMaxLevel/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public long getChildrenMaxLevel(@PathVariable long strId)
    {
        return this.strRepo.getChildrenMaxLevel(strRepo.getStrCode(strId));
    }

    @GetMapping(path = "/sigrh/structures/getAllChildren/{strId}") @ResponseBody @PreAuthorize("permitAll()")
    public List<ReadStrDTO> getChildren(@PathVariable long strId)
    {
        return this.strRepo.findAllChildren(strId).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList());
    }
}
