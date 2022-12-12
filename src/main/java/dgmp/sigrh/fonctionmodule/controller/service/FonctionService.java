package dgmp.sigrh.fonctionmodule.controller.service;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionHistoDAO;
import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionRepo;
import dgmp.sigrh.fonctionmodule.model.dtos.CreateFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.dtos.FonctionMapper;
import dgmp.sigrh.fonctionmodule.model.dtos.ReadFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.dtos.UpdateFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.fonctionmodule.model.events.FonctionEventType;
import dgmp.sigrh.fonctionmodule.model.histo.FonctionHisto;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class FonctionService implements IFonctionService
{
    private final FonctionMapper fonctionMapper;
    private final FonctionRepo fonctionRepo;
    private final FonctionHistoDAO fonctionHistoDAO;
    private final IHistoService<Fonction, FonctionHisto, FonctionEventType> fonctionHistoService;
    @Override
    public List<ReadFonctionDTO> getAllFonctions()
    {
        return fonctionRepo.findAll().stream().map(fonctionMapper::mapToReadFonctionDTO).collect(Collectors.toList());
    }

    @Override @Transactional
    public ReadFonctionDTO createFonction(CreateFonctionDTO dto)
    {
        Fonction fonction = fonctionRepo.save(fonctionMapper.mapToFonction(dto));
        fonctionHistoService.storeEntity(fonction, FonctionEventType.CREATE_FONCTION);
        return fonctionMapper.mapToReadFonctionDTO(fonction);
    }

    @Override @Transactional
    public ReadFonctionDTO updateFonction(UpdateFonctionDTO dto)
    {
        Fonction fonction = fonctionRepo.findById(dto.getIdFonction()).orElse(null);
        if(fonction != null)
        {
            fonction.setNomFonction(dto.getNomFonction().trim().toUpperCase());
            fonction.setFonctionDeNomination(dto.isFonctionDeNomination());
            fonction.setFonctionTopManager(dto.isFonctionTopManager());
            fonctionRepo.save(fonction);
            fonctionHistoService.storeEntity(fonction, FonctionEventType.UPDATE_FONCTION);
        }
        return fonctionMapper.mapToReadFonctionDTO(fonction);
    }

    @Override
    public Page<ReadFonctionDTO> searchPageOfFonctions(String searchKey, int pageNum, int pageSize)
    {
        Page<Fonction> fonctionsPage = fonctionRepo.searchPageFonction(searchKey, PageRequest.of(pageNum, pageSize));
        return new PageImpl<>(fonctionsPage.stream().map(fonctionMapper::mapToReadFonctionDTO).collect(Collectors.toList()), PageRequest.of(pageNum, pageSize), fonctionRepo.countActiveBySearchKey(searchKey));
    }

    @Override
    public Page<ReadFonctionDTO> searchPageOfDeletedFonctions(String searchKey, int pageNum, int pageSize)
    {
        Page<Fonction> fonctionsPage = fonctionRepo.searchDeletedPageFonction(searchKey, PageRequest.of(pageNum, pageSize));
        return new PageImpl<>(fonctionsPage.stream().map(fonctionMapper::mapToReadFonctionDTO).collect(Collectors.toList()), PageRequest.of(pageNum, pageSize), fonctionRepo.countDeletedBySearchKey(searchKey));
    }

    @Override
    @Transactional
    public void deleteFonction(Long idFonction)
    {
        Fonction fonction = fonctionRepo.findById(idFonction).orElse(null);
        if(fonction==null) return;
        if(fonction.getStatus()== PersistenceStatus.DELETED) return;
        fonction.setStatus(PersistenceStatus.DELETED);
        fonctionHistoService.storeEntity(fonction, FonctionEventType.DELETE_FONCTION);
    }

    @Override @Transactional
    public void restoreFonction(Long idFonction)
    {
        Fonction fonction = fonctionRepo.findById(idFonction).orElse(null);
        if(fonction==null) return;
        if(fonction.getStatus()==PersistenceStatus.ACTIVE) return;
        fonction.setStatus(PersistenceStatus.ACTIVE);
        fonctionHistoService.storeEntity(fonction, FonctionEventType.RESTORE_FONCTION);
    }



    @Override
    public List<FonctionHisto> getModificationHisto(Long idFonction)
    {
        return fonctionHistoDAO.getModificationsList(idFonction);
    }

    @Override
    public List<ReadFonctionDTO> getDeletedFonctions() {
        return fonctionRepo.getDeletedFonctions().stream().map(fonctionMapper::mapToReadFonctionDTO).collect(Collectors.toList());
    }
}
