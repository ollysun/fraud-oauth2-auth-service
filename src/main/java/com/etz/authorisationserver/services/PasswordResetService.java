package com.etz.authorisationserver.services;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.ResetPasswordRepository;
import com.etz.authorisationserver.repository.UserRepository;
import com.etz.authorisationserver.util.RandomTokenUtil;
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
	
	
    public Boolean resetUserPassword(String email, String userName) {
    	 Calendar now = Calendar.getInstance();
        now.setTimeInMillis(new Date().getTime());
        now.add(Calendar.MINUTE, 30);

       Date tokenCreationDate = new Date(now.getTime().getTime());
    	//check if user exists in the db
    	
    	// fetch user by emailOrusername if not throw exception "user not found"
    	// generate token(hash key), save to resetpasswordToken entity,
    	
    	Optional<UserEntity> userOptional = userRepository.findByEmailOrUsername(email, userName);
    	
    	if (!userOptional.isPresent()) {
    		throw new ResourceNotFoundException("no user with such email");
			
		}
    	//generate token and send to the supplied email then return true(Boolean.true)
    	//UUID uuid = Uuid5.fromUTF8(String.valueOf(System.currentTimeMillis()));//read up uuid
    	
    	 String tokenGenerated = String.valueOf(Uuid5.fromUTF8(String.valueOf(System.currentTimeMillis())));
    		
    	 
    	 ResetPasswordTokens resetPasswordTokens = new ResetPasswordTokens();
    	
    	 resetPasswordTokens.setToken(tokenGenerated);
    	 
    	 resetPasswordTokens.setConsumed(false);
    	 
    	 resetPasswordTokens.setExpirationDate(now);//this used LocalDateTime b4, how do u use the current Calender to know if token has expired?
    	 
    	 resetPasswordTokens.setExpired(false);
    	 
    	 resetPasswordTokens.setUserId(userOptional.get());
    	 //save this resetPasswordToken Entity to the db via a resetPasswordrepository which u will create
    	 resetPasswordRepository.save(resetPasswordTokens);
    	 //send a mail to the user's email, u have to create a method for this(verify with Moses)
    	 //autowire emailsender service and call its sendmail()
    	 
    	 String link = "<html><body><a href=\"http://localhost:9191/api/v1/password/token/"+ tokenGenerated+ "\">click on the link below</a></body></html>";
    	 //String link = "click on the link below \n http://localhost:9191/api/v1/password/token?user="+userOptional.get().getId()+"&token="+ tokenGenerated;
    	 emailSenderService.sendEmail(email, "token generation request", "token "+tokenGenerated+" has been generated.\n"+ link);
    	 return Boolean.TRUE;//return true in the end
    	
	}
    
    public Boolean checkTokenValidity(String userId, String token) {
    	
    	ResetPasswordTokens savedUserResetPasswordTokens = resetPasswordRepository.findByToken(token);
    	savedUserResetPasswordTokens.setConsumed(true);//bcos email link has been clicked
    	
    	Calendar tokenCreationDateTime = savedUserResetPasswordTokens.getExpirationDate();//get the expiration time of the token
    	  Date tokenCreationDate = new Date(tokenCreationDateTime.getTimeInMillis());//convert to a date object for comparison/checking
    	//Duration duration = Duration.between(tokenCreationDateTime, LocalDateTime.now());
    	Date tokenExpirationCheckTime = new Date();//create a new date as reference
    	  if (savedUserResetPasswordTokens.getToken()!=null&&tokenExpirationCheckTime.after(tokenCreationDate)) {
    		  savedUserResetPasswordTokens.setExpired(true);
  			 
    		  //redirect to an error page to be provided by Tunde Osbornreturn "redirect:/login.html?lang="
  			//then save to the db, get clarification on this..shd the token be deleted?
  			return Boolean.FALSE;
		}
    	 //redirect to password page to be provided by the front end dude return "redirect:/login.html?lang="
    	return Boolean.TRUE;
	}
    
    public Boolean updatePassword(String userId, String password) {
    	//get the user with the supplied token/id from the db    	
    UserEntity user = userRepository.findByPassword(password).get();//this is a dummy line of code!
    	//update/change the users password field 
    	user.setPassword(password);
    	
    	//save the user 
    	userRepository.save(user);
    	
    	return Boolean.TRUE;//for testing purposes?
    }
    
    public Boolean changePassword(String oldPassword, String newPassword) {
		//this user is authenticated/logged in..how do u get the him from this fact? 
    	return null;
	}
}
