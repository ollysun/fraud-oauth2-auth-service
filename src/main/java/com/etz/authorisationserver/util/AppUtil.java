package com.etz.authorisationserver.util;

import com.etz.authorisationserver.entity.ResetPasswordTokens;
import com.etz.authorisationserver.entity.UserEntity;
import com.etz.authorisationserver.exception.AuthServiceException;
import com.etz.authorisationserver.exception.ResourceNotFoundException;
import com.etz.authorisationserver.repository.ResetPasswordRepository;
import com.etz.authorisationserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.transaction.Transactional;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.time.LocalDateTime;

@Slf4j
@Transactional
public class AppUtil {

    @Autowired
    private static UserRepository userRepository;
    @Autowired
    private static ResetPasswordRepository resetPasswordRepository;
    @Autowired
    private static PasswordEncoder  passwordEncoder;



    private AppUtil() {}


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

}
