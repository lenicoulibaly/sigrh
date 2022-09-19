package dgmp.sigrh.structuremodule.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PostParamId implements Serializable
{
    private Long postGroupId;
    private Long emploiId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostParamId)) return false;
        PostParamId that = (PostParamId) o;
        return Objects.equals(postGroupId, that.postGroupId) && Objects.equals(emploiId, that.emploiId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postGroupId, emploiId);
    }
}
