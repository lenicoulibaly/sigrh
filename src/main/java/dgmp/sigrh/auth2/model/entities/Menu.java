package dgmp.sigrh.auth2.model.entities;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Menu
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_generator")
    @SequenceGenerator(name="menu_generator", sequenceName = "menu_seq", allocationSize=50)
    private Long menuId;
    @Column(unique = true)
    private String menuCode;

    @Column(columnDefinition = "text[]")
    @Type(type = "dgmp.sigrh.shared.model.customTypes.CustomStringArrayType")
    private String[] prvsCodes;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
}