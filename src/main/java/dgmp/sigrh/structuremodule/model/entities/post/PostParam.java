package dgmp.sigrh.structuremodule.model.entities.post;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.embedded.PostParamId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @NoArgsConstructor @AllArgsConstructor @Getter @Setter
@IdClass(PostParamId.class)
public class PostParam
{
    @Id
    private Long postGroupId;
    @Id
    private Long emploiId;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
}
