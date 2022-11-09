package dgmp.sigrh.auth2.model.dtos.asignation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrvsAssDTO
{
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private List<Long> prvIds;
    @ExistingAssId
    private Long principalAssId;
}
