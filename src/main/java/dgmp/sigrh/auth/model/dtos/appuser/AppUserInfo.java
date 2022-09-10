package dgmp.sigrh.auth.model.dtos.appuser;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;

public interface AppUserInfo {
    Long getUserId();

    boolean isActive();

    String getEmail();

    boolean isNotBlocked();

    String getTel();

    String getUsername();

    StructureInfo getStructure();

    interface StructureInfo {
        Long getStrId();

        long getStrLevel();

        String getStrName();

        Structure getStrParent();

        String getStrSigle();
    }
}
