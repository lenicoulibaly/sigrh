package dgmp.sigrh.archivemodule.controller.web;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.archivemodule.controller.repositories.ArchiveRepo;
import dgmp.sigrh.archivemodule.controller.service.IArchiveService;
import dgmp.sigrh.archivemodule.model.dtos.*;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.archivemodule.model.enums.ArchiveTypes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class ArchiveController
{
    private final ArchiveRepo archiveRepo;
    private final AgentRepo agentRepo;
    private final IArchiveService archiveService;
    private final ArchiveMapper archiveMapper;
    @GetMapping(path = "/sigrh/archive/download/{archiveId}")
    public ResponseEntity<Resource> downloadResource(@PathVariable Long archiveId, HttpServletResponse response) throws IOException
    {
        String filePath = archiveRepo.getArchivePath(archiveId);
        if(filePath==null) return null;
        String fileName = FilenameUtils.getName(filePath);
        File file = new File(filePath);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @GetMapping(path = "/sigrh/archives/civil-archives-form")
    public String gotoCivilArchivesForm(Model model, @RequestParam(defaultValue = "") String uniqueKey)
    {
        uniqueKey = uniqueKey == null ? "" : uniqueKey.trim();
        Agent agent = null;
        AddCivilArchivesDTO dto = null;
        if(agentRepo.existsByMatricule(uniqueKey)) agent = agentRepo.findByMatricule(uniqueKey);
        if(agentRepo.existsByEmail(uniqueKey)) agent = agentRepo.findByEmail(uniqueKey);
        if(agentRepo.existsByEmailPro(uniqueKey)) agent = agentRepo.findByEmailPro(uniqueKey);
        if(agentRepo.existsByTel(uniqueKey)) agent = agentRepo.findByTel(uniqueKey);
        if(agent != null)
        {
            dto = new AddCivilArchivesDTO();
            dto.setAgtId(agent.getAgentId());
        }
        model.addAttribute("agent", agent);
        model.addAttribute("dto", dto);

        return "personnel/civilarchiveForm";
    }

    @GetMapping(path = "/sigrh/archives/create-archive-form")
    public String gotoCreateArchiveForm(Model model, @RequestParam(defaultValue = "") String uniqueKey, ArchiveReqDTO dto)
    {
        uniqueKey = uniqueKey == null ? "" : uniqueKey.trim();
        Agent agent = null;

        if(agentRepo.existsByMatricule(uniqueKey)) agent = agentRepo.findByMatricule(uniqueKey);
        if(agentRepo.existsByEmail(uniqueKey)) agent = agentRepo.findByEmail(uniqueKey);
        if(agentRepo.existsByEmailPro(uniqueKey)) agent = agentRepo.findByEmailPro(uniqueKey);
        if(agentRepo.existsByTel(uniqueKey)) agent = agentRepo.findByTel(uniqueKey);
        if(agent != null)
        {
            dto = dto == null ? new ArchiveReqDTO() : dto;
            dto.setAgtId(agent.getAgentId());
        }
        model.addAttribute("agent", agent);
        model.addAttribute("dto", dto);

        return "personnel/createArchiveForm";
    }

    @PostMapping(path = "/sigrh/archives/add-civil-archives")
    public String addCivilArchives(Model model, RedirectAttributes ra, AddCivilArchivesDTO dto)
    {
        ra.addAttribute("uniqueKey", agentRepo.getEmailByAgtId(dto.getAgtId()));
        return "redirect:/sigrh/archives/civil-archives-form";
    }

    @PostMapping(path = "/sigrh/archives/create-archive")
    @Validated(OnCreate.class)
    public String createArchive(Model model, RedirectAttributes ra, @Valid ArchiveReqDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(fe -> model.addAttribute( fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            List<String> globalErrors = new ArrayList<>();
            br.getGlobalErrors().forEach(ge->
            {
                if(ge.getDefaultMessage() != null)
                {
                    if(ge.getDefaultMessage().contains("::"))
                    {
                        String[] errTab = ge.getDefaultMessage().split("::");
                        model.addAttribute(errTab[0] + "ErrMsg", errTab[1]);
                    }
                    else
                    {
                        globalErrors.add(ge.getDefaultMessage());
                    }
                }
            });

            model.addAttribute("globalErrs", globalErrors);
            model.addAttribute("sizeErrorMsg", ArchiveTypes.getSizeErrorMsg(dto.getArchiveTypeCode()));
            model.addAttribute("extensionErrorMsg", ArchiveTypes.getExtensionErrorMsg(dto.getArchiveTypeCode()));
            return gotoCreateArchiveForm(model, agentRepo.getEmailByAgtId(dto.getAgtId()), dto);
        }
        Archive archive = archiveService.createArchive(dto);
        ra.addAttribute("uniqueKey", agentRepo.getEmailByAgtId(dto.getAgtId()));
        return "redirect:/sigrh/archives/create-archive-form";
    }

    @GetMapping(path = "/sigrh/archives/update-archive-form")
    public String gotUpdateArchiveForm(Model model, @RequestParam Long archiveId, ArchiveReqDTO dto, @RequestParam(defaultValue = "false") boolean hasError)
    {
        Archive archive = archiveRepo.findById(archiveId).orElse(null);
        Agent agent = null;
        if(archive != null)
        {
            agent = agentRepo.findById(archive.getAgent().getAgentId()).get();
            dto = hasError ? dto : archiveMapper.mapToArchiveReqDTO(archive);
        }
        model.addAttribute("agent", agent);
        model.addAttribute("dto", dto);
        return "personnel/updateArchiveForm";
    }

    @PostMapping(path = "/sigrh/archives/update-archive")
    @Validated(OnUpdate.class)
    public String updateArchive(Model model, RedirectAttributes ra, @Valid ArchiveReqDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(fe -> model.addAttribute( fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            List<String> globalErrors = new ArrayList<>();
            br.getGlobalErrors().forEach(ge->
            {
                if(ge.getDefaultMessage() != null)
                {
                    if(ge.getDefaultMessage().contains("::"))
                    {
                        String[] errTab = ge.getDefaultMessage().split("::");
                        model.addAttribute(errTab[0] + "ErrMsg", errTab[1]);
                    }
                    else
                    {
                        globalErrors.add(ge.getDefaultMessage());
                    }
                }
            });

            model.addAttribute("globalErrs", globalErrors);
            model.addAttribute("sizeErrorMsg", ArchiveTypes.getSizeErrorMsg(dto.getArchiveTypeCode()));
            model.addAttribute("extensionErrorMsg", ArchiveTypes.getExtensionErrorMsg(dto.getArchiveTypeCode()));
            return gotUpdateArchiveForm(model, dto.getArchiveId(), dto, true);
        }
        Archive archive = archiveService.updateArchive(dto);
        ra.addAttribute("agtId", dto.getAgtId());
        ra.addAttribute("tab", "docs");
        return "redirect:/sigrh/agents/profile";
    }

}
