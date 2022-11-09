package dgmp.sigrh.auth2.model.events.types.auth;


import dgmp.sigrh.shared.model.enums.EventType;

public enum AssignationEventTypes implements EventType
{
    PRINCIPAL_ASSIGNATION_CREATED("Création d'une nouvelle assignation principale"),
    ASSIGNATION_UPDATED("ASSIGNATION_UPDATED"),
    ROLE_ASSIGNED_TO_PRINCIPAL_ASS("Ajout de rôle à une assignation principale"),
    PRIVILEGE_ASSIGNED_TO_PRINCIPAL_ASS("Ajout d'un privilege à une assignation principale"),
    ROLE_REVOKED_TO_PRINCIPAL_ASS("Revocation d'un rôle à une assignation principale"),
    PRIVILEGE_REVOKED_TO_PRINCIPAL_ASS("Revocation d'un privilege à une assignation principale"),
    PRIVILEGE_RESTORED_TO_PRINCIPAL_ASS("Restauration d'un privilege d'une assignation principale"),
    PRIVILEGE_REMOVED_TO_PRINCIPAL_ASS("Suppression d'un privilege à une assignation principale"),

    PRIVILEGE_ASSIGNED_TO_ROLE("PRIVILEGE_ASSIGNED_TO_ROLE"),
    PRIVILEGE_REVOKED_TO_ROLE("PRIVILEGE_REVOKED_TO_ROLE"),

    ASSIGNATION_ACTIVATED("Activation d'une assigntion"),
    ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED("Activation d'une assigntion et Changement de la période de validité d'une assignation"),
    ASSIGNATION_DEACTIVATED("ASSIGNATION_DEACTIVATED"),
    ASSIGNATION_SET_AS_DEFAULT("ASSIGNATION_SET_AS_DEFAULT"),
    ASSIGNATION_SET_AS_NONE_DEFAULT("ASSIGNATION_SET_AS_NONE_DEFAULT"),
    VALIDITY_PERIOD_CHANGED("Changement de la période de validité d'une assignation");
    String name;
    AssignationEventTypes(String name)
    {
        this.name = name;
    }
    AssignationEventTypes() { }

    @Override
    public String toString() {
        return name;
    }
}
