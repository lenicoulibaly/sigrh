package dgmp.sigrh.auth2.controller.validators.Exceptions;

public class ErrorMsg
{
    public static final String PASSWORD_NOT_PROVIDED_ERROR_MSG = "Mot de passe non fourni";
    public static final String OLD_PASSWORD_NOT_PROVIDED_ERROR_MSG = "L'ancien mot de passe n'a pas été fourni";
    public static final String TOO_SHORT_PASSWORD_ERROR_MSG = "Mot de passe trop court";
    public static final String INCORRECT_CONFIRMATION_PASSWORD_ERROR_MSG = "Mot de passe de confirmation incorrect";

    public static final String USER_ID_NOT_PROVIDED_ERROR_MSG = "L'identifiant de l'utilisateur n'a pas été fourni";
    public static final String USERNAME_NOT_PROVIDED_ERROR_MSG = "Login non fourni";

    public static final String EMAIL_NOT_PROVIDED_ERROR_MSG = "Email non fourni";
    public static final String BAD_EMAIL_ERROR_MSG="Email incorrect";
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
}
