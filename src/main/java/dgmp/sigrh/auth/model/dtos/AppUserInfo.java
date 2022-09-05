package dgmp.sigrh.auth.model.dtos;

public interface AppUserInfo {
    Long getUserId();

    boolean isActive();

    String getEmail();

    String getTel();

    String getUsername();

    StructureInfo getStructure();

    interface StructureInfo {
        Long getStrId();

        long getStrLevel();

        String getStrName();
    }
}
