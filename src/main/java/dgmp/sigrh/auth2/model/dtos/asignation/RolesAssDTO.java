package dgmp.sigrh.auth2.model.dtos.asignation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RolesAssDTO
{
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<Long> roleIds;
    @ExistingAssId
    private Long principalAssId;
}
