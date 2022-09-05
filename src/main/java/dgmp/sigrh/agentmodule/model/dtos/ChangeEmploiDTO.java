package dgmp.sigrh.agentmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor @AllArgsConstructor @Builder @Data
public class ChangeEmploiDTO
{
    private Long agentId;
    private Long emploiId;
    private String acteFilePath;
    private MultipartFile acteFile;
}
