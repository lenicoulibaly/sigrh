package dgmp.sigrh.structuremodule.model.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetPostParamsDTO
{
    private Set<Long> removableEmpIds;
    private Set<Long> newEmpIdsToBeAdded;
}
