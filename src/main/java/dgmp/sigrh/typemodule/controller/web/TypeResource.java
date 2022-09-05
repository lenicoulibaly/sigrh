package dgmp.sigrh.typemodule.controller.web;

import dgmp.sigrh.typemodule.controller.repositories.TypeParamRepo;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.controller.services.ITypeService;
import dgmp.sigrh.typemodule.model.dtos.CreateTypeDTO;
import dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO;
import dgmp.sigrh.typemodule.model.dtos.TypeParamDTO;
import dgmp.sigrh.typemodule.model.dtos.UpdateTypeDTO;
import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Profile({"test", "prod"})
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/types") @Validated
public class TypeResource
{
    private final TypeRepo typeRepo;
    private final TypeParamRepo sousTypeParamRepo;
    private final ITypeService typeService;

    @GetMapping(path = "") @PreAuthorize("hasAuthority('DEV')")
    public List<ReadTypeDTO> getAllTypes()
    {
        return typeRepo.findAllTypes();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{groupCode}")
    public List<ReadTypeDTO> getByGroupCode(@PathVariable String groupCode)
    {
        return null; //typeRepo.findByGroupCode(groupCode);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/sousTypeOf/{parentId}")
    public List<ReadTypeDTO> getSousTypeOf(@PathVariable @Positive Long parentId)
    {
        return typeRepo.findSousTypeOf(parentId);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/isSousTypeOf/{parentId}/{childId}")
    public boolean isSousTypeOf(@PathVariable @Positive Long parentId, @PathVariable @Positive Long childId)
    {
        return typeRepo.isSousTypeOf(parentId, childId);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/isDeletable/{typeId}")
    public boolean isDeletable(@PathVariable @Positive Long typeId)
    {
        return typeRepo.isDeletable(typeId);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/existsByUniqueCode")
    public boolean existsByUniqueCode(@RequestParam(defaultValue = "0") Long typeId, @RequestParam String uniqueCode)
    {
        return typeId.equals(0) ? typeRepo.existsByUniqueCode(uniqueCode) : typeRepo.existsByUniqueCode(typeId, uniqueCode);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/existsById")
    public boolean existsById(@RequestParam Long typeId)
    {
        return typeRepo.existsById(typeId);
    }

    @PreAuthorize("hasAuthority('DEV')")
    @PostMapping(path = "/create") @ResponseStatus(HttpStatus.CREATED)
    public Type createType(@RequestBody @Valid CreateTypeDTO dto)
    {
        return typeService.createType(dto);
    }

    @PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/update") @ResponseStatus(HttpStatus.OK)
    public Type updateType(@RequestBody @Valid UpdateTypeDTO dto)
    {
        return typeService.updateType(dto);
    }

    @PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/setSousType") @ResponseStatus(HttpStatus.OK)
    public void setSousType(@RequestBody @Valid TypeParamDTO dto)
    {
        typeService.addSousType(dto);
    }

    @PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/removeSousType") @ResponseStatus(HttpStatus.OK)
    public void removeSousType(@RequestBody @Valid TypeParamDTO dto)
    {
        typeService.removeSousType(dto);
    }
}
