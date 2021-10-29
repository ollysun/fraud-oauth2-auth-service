package com.etz.authorisationserver.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.etz.authorisationserver.config.UserNotificationUrlConfig;
import com.etz.authorisationserver.dto.request.UserNotification;
import com.etz.authorisationserver.dto.request.UserNotificationRequest;
import com.etz.authorisationserver.dto.response.ModelResponse;
import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.ResetPasswordRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Component
public class AppUtil {

    @Autowired
    private static ResetPasswordRepository resetPasswordRepository;
    @Autowired
    private static PasswordEncoder  passwordEncoder;

    @Autowired
    private ApiUtil apiUtil;
    
    @Autowired
    private UserNotificationUrlConfig eagelEyeManager;


    public AppUtil() {}


    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static boolean isBlank(Object text) {
        String textStr = (String) text;
        return isBlank(textStr);
    }


    public static LocalDateTime setTokenValidityPeriod(){
        try {
            return LocalDateTime.now().plusMinutes(30);
        }catch(DateTimeException ex){
            throw new AuthServiceException("exception on add 30 minutes");
        }
    }

    public static ResetPasswordTokens validatePasswordResetToken(String encryptdetails) {

        String decryption = null;
        try {
            decryption = AESUtil.decrypt(encryptdetails);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.debug(e.getMessage());
            throw new AuthServiceException(e.getMessage());
        }
        String[] splitDescrypt = decryption.split("-");
        ResetPasswordTokens passToken = resetPasswordRepository.findByUserIdAndTokenAndExpiredFalse(Long.parseLong(splitDescrypt[0]), splitDescrypt[1])
                                                        .orElseThrow(() -> new ResourceNotFoundException("Invalid Token"));
        if (!isTokenExpired(passToken)){
            throw new AuthServiceException("Token Expired");
        }
        return passToken;
    }

    private static boolean isTokenExpired(ResetPasswordTokens passToken) {
        return passToken.getExpirationDate().isBefore(LocalDateTime.now());
    }

    public static boolean checkIfValidOldPassword(String oldPassword, UserEntity user){
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    
    public int createUserNotification(String entity, String entityId, String createdBy) {
    	UserNotificationRequest userNotificationRequest = new UserNotificationRequest();
    	userNotificationRequest.setEntity(entity);
    	userNotificationRequest.setEntityId(entityId);
    	userNotificationRequest.setNotifType(1);
    	//userNotificationRequest.setRoleId()
    	//userNotificationRequest.setUserId()
    	userNotificationRequest.setCreatedBy(createdBy);

		int httpStatus = 0;
		try {
			log.info("Create UserNotification URL : {}", eagelEyeManager.getCreateUserNotificationUrl());
			log.info("Create UserNotification Request : {}", userNotificationRequest);
			@SuppressWarnings("unchecked")
			ModelResponse<UserNotification> userNotificationResponse = apiUtil.post(eagelEyeManager.getCreateUserNotificationUrl(), setFixedExtraHeader(), userNotificationRequest, 
					(Class<ModelResponse<UserNotification>>) (Class<?>) ModelResponse.class);

			log.info("create user notification http response status >>> {}", userNotificationResponse.getStatus());
			log.info("User notification response object >>> {}", userNotificationResponse.getData());
		
			httpStatus = userNotificationResponse.getStatus();
		} catch(Exception ex) {
			log.error("Call to create user notification endpoint failed >>> {}", ex);
			throw new AuthServiceException("Could not create user notification");
		}
		
		return httpStatus;
    }
    
	private static HashMap<String, String> setFixedExtraHeader() {
		HashMap<String, String> extraHeaders = new HashMap<>();
		extraHeaders.put(HttpHeaders.AUTHORIZATION, "Bearer " + RequestUtil.getToken());
		extraHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return extraHeaders;
	}
    
}
