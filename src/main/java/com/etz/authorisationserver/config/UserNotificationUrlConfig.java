package com.etz.authorisationserver.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.eagle-eye-manager")
public class UserNotificationUrlConfig {

	@NotBlank(message = "Eagle Eye Manager User Notification URL is required!")
	private String createUserNotificationUrl;
}
