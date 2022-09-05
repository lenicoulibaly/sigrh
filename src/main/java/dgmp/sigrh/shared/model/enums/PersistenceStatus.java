package dgmp.sigrh.shared.model.enums;

import lombok.Getter;

public enum PersistenceStatus
{
    ACTIVE("Actif"),
    DELETED("Suppimé"),
    STORED("Historisé");

    @Getter
    public String name;

    PersistenceStatus(String name) {
        this.name = name;
    }
}
