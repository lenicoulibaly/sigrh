package dgmp.sigrh.agentmodule.model.projections;

public interface StructureInfo
{
    Long getStrId();

    long getStrLevel();

    String getStrName();

    String getStrSigle();

    StrParentInfo getStrParent();

    interface StrParentInfo
    {
        Long getStrId();

        long getStrLevel();

        String getStrName();

        String getStrSigle();
    }
}
