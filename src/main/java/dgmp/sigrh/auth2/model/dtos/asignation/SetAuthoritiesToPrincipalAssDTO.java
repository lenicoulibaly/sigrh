package dgmp.sigrh.auth2.model.dtos.asignation;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SetAuthoritiesToPrincipalAssDTO
{
    @ExistingAssId
    private Long principalAssId;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private Set<Long> roleIds;
    private Set<Long> prvIds;
}