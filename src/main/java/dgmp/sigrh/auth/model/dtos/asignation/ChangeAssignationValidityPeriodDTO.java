package dgmp.sigrh.auth.model.dtos.asignation;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChangeAssignationValidityPeriodDTO
{
    @ExistingAssId
    private Long assId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
