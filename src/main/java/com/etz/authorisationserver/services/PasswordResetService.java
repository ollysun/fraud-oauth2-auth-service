package com.etz.authorisationserver.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.authorisationserver.constant.AppConstant;
import com.etz.authorisationserver.dto.request.ChangePasswordRequestModel;
import com.etz.authorisationserver.dto.request.PasswordDto;
import com.etz.authorisationserver.dto.request.ResetTokenRequestModel;
import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.ResetPasswordRepository;
import com.etz.authorisationserver.repository.UserRepository;
import com.etz.authorisationserver.util.AESUtil;
import com.etz.authorisationserver.util.AppUtil;
import com.etz.authorisationserver.util.RequestUtil;
import com.etz.authorisationserver.util.Uuid5;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordResetService {

	
	private final UserRepository userRepository;
	private final ResetPasswordRepository resetPasswordRepository;
	private final EmailSenderService emailSenderService;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
    public Boolean resetUserPassword(ResetTokenRequestModel resetTokenRequestModel) {
    	//validate user credential with username and email
    	UserEntity userOptional = userRepository.findByEmailAndUsername(resetTokenRequestModel.getEmail(),
				resetTokenRequestModel.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("No user found with this credential"));

    	//generate token and send to the supplied email then return true(Boolean.true)
    	 String tokenGenerated = Uuid5.fromUTF8(String.valueOf(System.currentTimeMillis())).toString();

    	 ResetPasswordTokens resetPasswordTokens = new ResetPasswordTokens();
    	 resetPasswordTokens.setToken(tokenGenerated);
    	 resetPasswordTokens.setConsumed(false);
    	 resetPasswordTokens.setExpirationDate(AppUtil.setTokenValidityPeriod());
    	 resetPasswordTokens.setExpired(false);
    	 resetPasswordTokens.setUserId(userOptional);
    	 //save this resetPasswordToken Entity to the db via a resetPasswordrepository which u will create
    	 resetPasswordRepository.save(resetPasswordTokens);
    	 //send a mail to the user's email, u have to create a method for this(verify with Moses)

		String encryptUserDetail = null;
		try {
			encryptUserDetail = AESUtil.encrypt(userOptional.getId()+"-"+tokenGenerated);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			//log.debug(e.getMessage());
			throw new AuthServiceException(e.getMessage());
		}
		//autowire emailsender service and call its sendmail()
    	 String link = "<html><body><a href=\"http://172.17.10.83:3004/reset?user="+encryptUserDetail +"\">click on the link below</a></body></html>";
    	 emailSenderService.sendEmail(resetTokenRequestModel.getEmail(), "token generation request", "token "+tokenGenerated+" has been generated.\n"+ link);
    	 return Boolean.TRUE;//return true in the end
    	
	}

	public String showChangePasswordPage(String encryptUserDetail){
    	ResetPasswordTokens passToken = validatePasswordResetToken(encryptUserDetail);
		resetPasswordRepository.save(passToken);
		//TODO: tunde to provide redirecturl and pass encrypt userdetail as queryparam;
		return "http://172.17.10.83:3004/resetpassword?userdetail=" + encryptUserDetail;
	}


	@Transactional
    public Boolean updatePassword(String encryptUserDetail, PasswordDto passwordDto) {

		ResetPasswordTokens passToken = validatePasswordResetToken(encryptUserDetail);
		passToken.setConsumed(Boolean.TRUE);
		if (LocalDateTime.now().isAfter(passToken.getExpirationDate())){
			passToken.setExpired(Boolean.TRUE);
			throw new AuthServiceException("Token expired. Kindly restart the process");
		}
		resetPasswordRepository.save(passToken);

		UserEntity userEntity = passToken.getUserId();
		userEntity.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
		userRepository.save(userEntity);

		//TODO: Notify user of new password
    	return Boolean.TRUE;
    }
    
    public Boolean changePassword(ChangePasswordRequestModel changePasswordRequestModel) {
		log.info("{}", RequestUtil.getAccessTokenClaim(AppConstant.TOKEN_USERNAME));
		final UserEntity user = userRepository.findByUsername(RequestUtil.getAccessTokenClaim(AppConstant.TOKEN_USERNAME));
		if (user == null){
			log.error("Unauthenticated User cannot change Password. Username -> {}", RequestUtil.getAccessTokenClaim(AppConstant.TOKEN_USERNAME));
			throw new AuthServiceException("Unauthenticated User cannot change Password");
		}

		if (!checkIfValidOldPassword(changePasswordRequestModel.getOldPassword(), user)) {
			log.error("Invalid old Password for Username {}", user.getUsername());
			throw new AuthServiceException("Invalid old Password");
		}
		user.setPassword(passwordEncoder.encode(changePasswordRequestModel.getNewPassword()));
		userRepository.save(user);
		//TODO: mail Notify user of new password
		return Boolean.TRUE;
	}
    // checking the password against user details object
    private boolean checkIfValidOldPassword(String oldPassword, UserEntity user){
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    
    private ResetPasswordTokens validatePasswordResetToken(String encryptdetails) {

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
        if (!AppUtil.isTokenExpired(passToken)){
            throw new AuthServiceException("Token Expired");
        }
        return passToken;
    }
    
}
