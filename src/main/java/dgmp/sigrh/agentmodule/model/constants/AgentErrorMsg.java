package dgmp.sigrh.agentmodule.model.constants;

import java.time.LocalDate;

public class AgentErrorMsg
{
    public static final String NONE_DATA_PROVIDED_ERROR_MSG = "Veuillez fournir les données relatives à l'agent";
    public static final String AGENT_ID_NOT_FOUND_ERROR_MSG = "idAgent introuvable";
    public static final String NOM_NULL_ERROR_MSG = "Le nom de l'agent ne peut être null";
    public static final String PRENOM_NULL_ERROR_MSG = "Le prénom de l'agent ne peut être null";
    public static final String CIVILITE_INCORRECT_ERROR_MSG = "La civilité doit être MONSIEUR, MADAME ou MADEMOISELLE";
    public static final String EMAIL_PRO_ALREADY_EXISTS_ERROR_MSG = "Cette adresse mail est déjà utilisée";
    public static final String EMAIL_NOT_PROVIDED_ERROR_MSG = "Email non fourni";
    public static final String BAD_EMAIL_ERROR_MSG="Email incorrect";
    public static final String DATE_NAISSANCE_NOT_PROVIDED_ERROR_MSG = "La date de naissance ne peut être nulle";
    public static final String TEL_NOT_PROVIDED_ERROR_MSG = "Numéro de téléphone non fourni";
    public static final String USER_ID_NOT_FOUND_ERROR_MSG = "L'identifiant de l'utilisateur est inexistant";
    public static final String USERNAME_NOT_FOUND_ERROR_MSG = "Login inexistant";
    public static final String USERNAME_ALREADY_EXISTS_ERROR_MSG = "Login déjà créé";
    public static final String EMAIL_ALREADY_EXISTS_ERROR_MSG = "Email déjà attribué";
    public static final String TEL_ALREADY_EXISTS_ERROR_MSG = "Numéro de téléphone déjà attribué";
    public static final String ROLE_ID_NOT_FOUND_ERROR_MSG = "Role inexistant";
    public static final String ROLE_NAME_NOT_FOUND_ERROR_MSG = "Nom de Role inexistant";
    public static final String ROLE_DELETION_NOT_ALLOWED_ERROR_MSG = "Ce role fait l'objet d'assignation(s). Il ne peut être supprimé";
    public static final String ROLE_NAME_ALREADY_EXIST_ERROR_MSG = "Role déjà créé";
    public static final String ROLE_NAME_NOT_PROVIDED_ERROR_MSG = "Nom de role non fourni";
    public static final String ROLE_CODE_NOT_PROVIDED_ERROR_MSG="Code de role non fourni";
    public static final String ROLE_CODE_ALREADY_EXIST_ERROR_MSG="Code de role déjà attribué";
    public static final String INVALID_ROLE_ID_ERROR_MSG = "Identifiant de rôle invalide";
    public static final String PRIVILEGE_NAME_NOT_FOUND_ERROR_MSG = "Privilège inexistant";
    public static final String PRIVILEGE_NAME_NOT_PROVIDED_ERROR_MSG = "Nom de privilège non fournie";
    public static final String ALREADY_EXISTING_PRIVILEGE_ERROR_MSG = "Privilège déjà créé";

    public static final String UNEXPECTED_ERROR_MSG = "Une erreur inattadue s'est produite";
    public static final String UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG = "Token inconnu";
    public static final String TOO_YOUNG_ERROR_MSG = "La date de naissance ne peut être postérieur à" + LocalDate.now().minusYears(15);
    public static final String TOO_OLD_ERROR_MSG = "La date de naissance ne peut être ultérieure à" + LocalDate.now().minusYears(100);
    public static final String TYPE_PIECE_INCORRECT_ERROR_MSG = "Type de piece inconnu";
    public static final String NUM_PIECE_ALREADY_EXISTS_ERROR_MSG = "Numéro de piece déjà utilisé";
}
