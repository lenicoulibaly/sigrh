package dgmp.sigrh.structuremodule.model.dtos.str;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StrTreeView
{
    private String text;
    private String href;
    private String icon;
    private List<StrTreeView> nodes;
}
