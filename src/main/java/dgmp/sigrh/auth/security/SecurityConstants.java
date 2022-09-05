package dgmp.sigrh.auth.security;

public class SecurityConstants
{
	public static final String SECRET = "kgjgvefhzefvjz";
	public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000*60*10;
	public static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000*60*30;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String JWT_HEADER = "Authorization";
	public static final String ACCESS_TOKEN_KEY = "accessToken";
	public static final String REFRESH_TOKEN_KEY = "refreshToken";

	public static final String ERROR_MSG_KEY = "error-msg";
	public static final String CONFIRMATION_LINK = "/users/account-confirmation?confirmationToken=";
	public static final String ACTIVATION_LINK = "/users/account-activation?activationToken=";
	public static final String REINITIALISE_PASSWORD_LINK = "/password-reinitialisation?pwdReinitToken=";

	public static final String USERNAME_CLAIM = "username";
	public static final String USER_ID_CLAIM = "userId";
	public static final String AUTHORITIES_CLAIM = "authorities";
	public static final String FUNCTION_ID_CLAIM = "functionId";
	public static final String FUNCTION_NAME_CLAIM = "functionName";
	public static final String FUNCTION_CODE_CLAIM = "functionCode";
	public static final String AGENT_ID_CLAIM = "agentId";
	public static final String ISSUER_CLAIM = "orion.staffadmin.authorization-server";

	public static final String USER_TOPIC="USER_TOPIC";
	public static final String TOKEN_TOPIC="TOKEN_TOPIC";
	public static final String EMAIL_NOTIFICATION_TOPIC = "EMAIL_NOTIFICATION_TOPIC";
	public static final String STRUCTURE_TOPIC = "STRUCTURE_TOPIC";
	public static final String AUTH_TOPIC = "AUTH_TOPIC";
	public static final String ASSIGNATION_TOPIC = "ASSIGNATION_TOPIC";

	public static String ACCOUNT_ACTIVATION_REQUEST_OBJECT = "Activation de votre compte";
	public static String PASSWORD_REINITIALISATION_REQUEST_OBJECT = "Réinitialisation de votre mot de passe";
}
