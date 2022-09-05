package dgmp.sigrh.typemodule.model.dtos;

import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ReadTypeDTO
{
    private Long typeId;
    private String typeGroup;
    private String uniqueCode;
    private String name;
    private String status;
    private List<ReadTypeDTO> children;


    public ReadTypeDTO(Type type) {
        BeanUtils.copyProperties(type, this);
    }
}