package dgmp.sigrh.structuremodule.model.dtos;

public interface StructureProjection
{
    Long getStrId();

    long getStrLevel();

    String getStrName();

    String getStrSigle();

    ParentProjection getStrParent();

    interface ParentProjection
    {
        Long getStrId();

        long getStrLevel();

        String getStrName();

        String getStrSigle();
    }
}
