package dgmp.sigrh.fonctionmodule.controller.service;

import dgmp.sigrh.fonctionmodule.model.dtos.CreateFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.dtos.ReadFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.dtos.UpdateFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.histo.FonctionHisto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFonctionService
{
    List<ReadFonctionDTO> getAllFonctions();
    ReadFonctionDTO createFonction(CreateFonctionDTO dto);
    ReadFonctionDTO updateFonction(UpdateFonctionDTO dto);
    Page<ReadFonctionDTO> searchPageOfFonctions(String searchKey, int pageNum, int pageSize);
    void deleteFonction(Long idFonction);
    void restoreFonction(Long idFonction);
    List<FonctionHisto> getModificationHisto(Long idFonction);
    List<ReadFonctionDTO> getDeletedFonctions();
    Page<ReadFonctionDTO> searchPageOfDeletedFonctions(String searchKey, int pageNum, int pageSize);

}
