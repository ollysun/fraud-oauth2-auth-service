package com.etz.authorisationserver.services;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import com.etz.authorisationserver.dto.request.ChangePasswordRequestModel;
import com.etz.authorisationserver.dto.request.PasswordDto;
import com.etz.authorisationserver.dto.request.ResetTokenRequestModel;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.util.AESUtil;
import com.etz.authorisationserver.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.ResetPasswordRepository;
import com.etz.authorisationserver.repository.UserRepository;
import com.etz.authorisationserver.util.Uuid5;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
			log.debug(e.getMessage());
			throw new AuthServiceException(e.getMessage());
		}
		//autowire emailsender service and call its sendmail()
    	 String link = "<html><body><a href=\"http://172.17.10.83:3004/reset?user="+encryptUserDetail +"\">click on the link below</a></body></html>";
    	 emailSenderService.sendEmail(resetTokenRequestModel.getEmail(), "token generation request", "token "+tokenGenerated+" has been generated.\n"+ link);
    	 return Boolean.TRUE;//return true in the end
    	
	}

	public String showChangePasswordPage(String encryptUserDetail){
    	ResetPasswordTokens passToken = AppUtil.validatePasswordResetToken(encryptUserDetail);
		resetPasswordRepository.save(passToken);
		//TODO: tunde to provide redirecturl and pass encrypt userdetail as queryparam;
		return "redirect url";
	}


	@Transactional
    public Boolean updatePassword(String encryptUserDetail, PasswordDto passwordDto) {

		ResetPasswordTokens passToken = AppUtil.validatePasswordResetToken(encryptUserDetail);
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
		final UserEntity user = userRepository.findByEmail(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
		if (user == null){
			throw new AuthServiceException("Unauthenticated User cannot change Password");
		}

		if (!AppUtil.checkIfValidOldPassword(changePasswordRequestModel.getOldPassword(), user)) {
			throw new AuthServiceException("Invalid old Password");
		}
		user.setPassword(passwordEncoder.encode(changePasswordRequestModel.getNewPassword()));
		userRepository.save(user);
		//TODO: Notify user of new password
		return Boolean.TRUE;
	}
}
