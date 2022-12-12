package dgmp.sigrh.emploimodule.controller.service;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiHistoDAO;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiRepo;
import dgmp.sigrh.emploimodule.model.dtos.CreateEmploiDTO;
import dgmp.sigrh.emploimodule.model.dtos.EmploiMapper;
import dgmp.sigrh.emploimodule.model.dtos.ReadEmploiDTO;
import dgmp.sigrh.emploimodule.model.dtos.UpdateEmploiDTO;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.emploimodule.model.events.EmploiEventType;
import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("emploiService") ///@Profile("flash")
@RequiredArgsConstructor
public class EmploiServiceFlash implements IEmploiService
{
    private final EmploiMapper emploiMapper;
    private final EmploiRepo emploiRepo;
    private final EmploiHistoDAO emploiHistoDAO;
    private final IHistoService<Emploi, EmploiHisto, EmploiEventType> emploiHistoService;
    @Override
    public List<ReadEmploiDTO> getAllEmplois()
    {
        return emploiRepo.findAll().stream().map(emploiMapper::mapToReadEmploiDTO).collect(Collectors.toList());
    }

    @Override
    public ReadEmploiDTO getEmploisById(Long empId) {
        return getAllEmplois().stream().filter(emp->emp.getIdEmploi().equals(empId)).findFirst().orElse(null);
    }

    @Override @Transactional
    public ReadEmploiDTO createEmploi(CreateEmploiDTO dto)
    {
        Emploi emploi = emploiRepo.save(emploiMapper.mapToEmploi(dto));
        emploiHistoService.storeEntity(emploi, EmploiEventType.CREATE_EMPLOI);
        return emploiMapper.mapToReadEmploiDTO(emploi);
    }

    @Override
    @Transactional
    public ReadEmploiDTO updateEmploi(UpdateEmploiDTO dto)
    {
        Emploi emploi = emploiRepo.findById(dto.getIdEmploi()).orElse(null);
        if(emploi != null)
        {
            emploi.setNomEmploi(dto.getNomEmploi().trim().toUpperCase());
            emploiHistoService.storeEntity(emploi, EmploiEventType.UPDATE_EMPLOI);
        }
        return emploiMapper.mapToReadEmploiDTO(emploi);
    }

    @Override @Transactional
    public void deleteEmploi(Long idEmploi)
    {
        Emploi emploi = emploiRepo.findById(idEmploi).orElse(null);
        if (emploi==null) return ;
        emploi.setStatus(PersistenceStatus.DELETED);
        emploiHistoService.storeEntity(emploi, EmploiEventType.DELETE_EMPLOI);
    }

    @Override @Transactional
    public void restoreEmploi(Long idEmploi)
    {
        Emploi emploi = emploiRepo.findById(idEmploi).orElse(null);
        if(emploi == null) return;
        if(emploi.getStatus() == PersistenceStatus.ACTIVE) return;
        emploi.setStatus(PersistenceStatus.ACTIVE);
        emploiHistoService.storeEntity(emploi, EmploiEventType.RESTORE_EMPLOI);
    }

    @Override
    public Page<ReadEmploiDTO> searchPageOfEmplois(String searchKey, int pageNum, int pageSize)
    {
        Page<Emploi> emploiPage = emploiRepo.searchPageEmploi(searchKey, PageRequest.of(pageNum, pageSize));
        return new PageImpl<>(emploiPage.stream().map(emploiMapper::mapToReadEmploiDTO).collect(Collectors.toList()), PageRequest.of(pageNum, pageSize), emploiRepo.countActiveBySearchKey(searchKey));
    }

    @Override
    public Page<ReadEmploiDTO> searchPageOfDeletedEmplois(String searchKey, int pageNum, int pageSize)
    {
        Page<Emploi> emploisPage = emploiRepo.searchDeletedPageEmploi(searchKey, PageRequest.of(pageNum, pageSize));
        return new PageImpl<>(emploisPage.stream().map(emploiMapper::mapToReadEmploiDTO).collect(Collectors.toList()), PageRequest.of(pageNum, pageSize), emploiRepo.countDeletedBySearchKey(searchKey));
    }

    @Override
    public List<EmploiHisto> getAllModificationHisto(long idEmploi) {
        return emploiHistoDAO.getModificationsList(idEmploi);
    }

    @Override
    public Page<EmploiHisto> getModificationHistoPage(long idEmploi, int pageNum, int pageSize)
    {
        Page<EmploiHisto> histoPage = emploiHistoDAO.getHistoPageBetweenPeriod(idEmploi, PageRequest.of(pageNum, pageSize));
        return new PageImpl<>(histoPage.toList(), PageRequest.of(pageNum, pageSize), emploiHistoDAO.countByIdEmploi(idEmploi));
    }
}
