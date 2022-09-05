package dgmp.sigrh.typemodule.controller.web;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.typemodule.controller.repositories.TypeParamRepo;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.controller.services.ITypeService;
import dgmp.sigrh.typemodule.model.dtos.CreateTypeDTO;
import dgmp.sigrh.typemodule.model.dtos.UpdateTypeDTO;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.entities.TypeHisto;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller @RequiredArgsConstructor
public class TypeController
{
    private final ITypeService typeService;
    private final TypeRepo typeRepo;
    private TypeParamRepo typeParamRepo;
    private final IHistoService<Type, TypeHisto, TypeEventType> typeHistoService;

    @GetMapping(path = "/sigrh/administration/types/types-list")
    public String gotoTypeList(Model model, @RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        Page<Type> types = typeService.searchPageOfTypes(key, key, pageNum, pageSize);
        model.addAttribute("types", types);
        model.addAttribute("types", new long[types.getTotalPages()]);
        model.addAttribute("viewMode", "list");
        return "administration/types/typesList";
    }

    @GetMapping(path = "/sigrh/administration/types/type-details")
    public String gotoTypeDetails(Model model, @RequestParam(defaultValue = "0") long typeId,
                                   @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "20") int pageSize,
                                   @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusYears(2)}") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime after,
                                   @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime befor)
    {
        Type type = typeRepo.findById(typeId).orElse(null);
        model.addAttribute("type", type == null ? new Type() : type);
        model.addAttribute("modificationList", typeHistoService.getHistoPageBetweenPeriod(typeId, after, befor, pageNum, pageSize));
        model.addAttribute("viewMode", "details");
        return "administration/types/typeDetails";
    }

    @GetMapping(path ="/sigrh/administration/types/deleted-types-list")
    public String gotoDeletedTypesList(Model model, @RequestParam(defaultValue = "")String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        Page<Type> types = typeService.searchPageOfDeletedTypes(key, key, pageNum, pageSize);
        model.addAttribute("types", types);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("types", types);
        model.addAttribute("pages", new long[types.getTotalPages()]);
        model.addAttribute("viewMode", "trash");
        return "administration/types/deletedTypesList";
    }

    @GetMapping(path = "/sigrh/administration/types/new-type-form")
    public String gotoNewTypesForm(Model model)
    {
        model.addAttribute("type", new CreateTypeDTO());
        model.addAttribute("viewMode", "new");
        return "administration/types/newTypeForm";
    }

    @GetMapping(path = "/sigrh/administration/types/update-type-form")
    public String gotoUpdateTypeForm(Model model, @RequestParam(defaultValue = "0") long typeId)
    {
        Type loadedType = typeRepo.findById(typeId).orElse(null);
        UpdateTypeDTO typeDTO = loadedType == null ? new UpdateTypeDTO() : new UpdateTypeDTO(loadedType.getTypeId(), loadedType.getTypeGroup().getGroupCode(), loadedType.getUniqueCode(), loadedType.getName());
        model.addAttribute("type", typeDTO);
        model.addAttribute("viewMode", "update");
        return "administration/types/updateTypeForm";
    }
}
