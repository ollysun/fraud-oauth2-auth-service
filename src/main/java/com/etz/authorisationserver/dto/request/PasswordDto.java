package com.etz.authorisationserver.dto.request;

import com.etz.authorisationserver.validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PasswordDto {
    @ValidPassword
    @NotBlank(message="Please provide the new password")
    private String newPassword;

}
