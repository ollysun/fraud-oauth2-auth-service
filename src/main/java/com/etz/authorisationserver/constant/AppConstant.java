package com.etz.authorisationserver.constant;

public interface AppConstant {
    int PAGE_SIZE = 50;
    String PAGE = "page";

    String CREATEDAT = "createdAt";
    String CREATED_AT = "created_at";
    String UPDATED_AT = "updated_at";

    String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

	public static final String USER = "USER";
	public static final String ROLE = "ROLE";
	
	public static final String AUTHORISATION_OBJECT = "AUTHORISATION_OBJECT";
	public static final String APPROVE_ACTION = "APPROVE";

	public static final String ACCESS_TOKEN_CLAIM = "access_token_claim";
	public static final String ACCESS_TOKEN = "token";
	public static final String AUTHORIZATION = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TOKEN_USERNAME = "user_name";
}