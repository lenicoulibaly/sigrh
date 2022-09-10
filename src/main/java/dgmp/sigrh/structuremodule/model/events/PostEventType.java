package dgmp.sigrh.structuremodule.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum PostEventType
{
    CREATE_POST("Création d'un post"),
    UPDATE_POST("modification d'un post"),
    DELETE_POST("Suppression d'un post"),
    RESTORE_POST("Restoration d'un post"),
    ADD_EMPLOI_TO_POST("Ajout d'un emploi compatible à un post"),
    RESTORE_EMPLOI_TO_POST("Restoration d'un emploi compatible à un post"),
    REMOVE_EMPLOI_TO_POST("Suppression d'un emploi compatible à post");
    private String event;
}
