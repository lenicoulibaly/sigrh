package dgmp.sigrh.structuremodule.model.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SetCompatibleEmpDTO
{
    private Set<Long> removableEmpIds;
    private Set<Long> newEmpIdsToBeAdded;
}
