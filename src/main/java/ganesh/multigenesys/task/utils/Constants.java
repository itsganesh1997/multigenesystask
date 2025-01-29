package ganesh.multigenesys.task.utils;

public class Constants {

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 8 * 60 * 60;  //  Token expired in 8 hours
	//public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60; //  Token expired in 5 minutes

	public static final String SIGNING_KEY = "ganeshTask";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
}
