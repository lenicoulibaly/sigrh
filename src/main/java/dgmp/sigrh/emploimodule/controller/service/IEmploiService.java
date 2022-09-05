package dgmp.sigrh.emploimodule.controller.service;

import dgmp.sigrh.emploimodule.model.dtos.CreateEmploiDTO;
import dgmp.sigrh.emploimodule.model.dtos.ReadEmploiDTO;
import dgmp.sigrh.emploimodule.model.dtos.UpdateEmploiDTO;
import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEmploiService
{

    ReadEmploiDTO createEmploi(CreateEmploiDTO dto);
    ReadEmploiDTO updateEmploi(UpdateEmploiDTO dto);
    void deleteEmploi(Long idEmploi);
    void restoreEmploi(Long idEmploi);

    Page<ReadEmploiDTO> searchPageOfEmplois(String searchKey, int pageNum, int pageSize);
    List<ReadEmploiDTO> getAllEmplois();
    ReadEmploiDTO getEmploisById(Long empId);

    Page<ReadEmploiDTO> searchPageOfDeletedEmplois(String searchKey, int pageNum, int pageSize);

    List<EmploiHisto> getAllModificationHisto(long idEmploi);

    Page<EmploiHisto> getModificationHistoPage(long idEmploi, int pageNum, int pageSize);
}
