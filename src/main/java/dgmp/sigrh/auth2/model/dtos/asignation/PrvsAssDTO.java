package dgmp.sigrh.auth2.model.dtos.asignation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrvsAssDTO
{
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<Long> prvIds = new HashSet<>();
    @ExistingAssId
    private Long principalAssId;
}
