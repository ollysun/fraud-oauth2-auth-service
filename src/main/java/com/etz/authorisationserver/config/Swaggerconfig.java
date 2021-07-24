package com.etz.authorisationserver.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swaggerconfig {
	//create a docket bean i.e a method that returns a Docket bean
		private static final String AUTHORIZATION = "Authorization";
			@Bean
			public Docket api() {
						
				return new Docket(DocumentationType.SWAGGER_2)
						.apiInfo(getApiInfo())
						.select()//returns the apiSelector builder instance that controls the endpoints exposed by swagger
						.apis(RequestHandlerSelectors.basePackage("com.etz.authorisationserver"))//specify controller classes(in a package) u wanna expose
						.paths(PathSelectors.any())//specify the controller  that you wanna expose, using their endpoint pattern(antpattern)
						.build()//build the docket
						.securitySchemes(Collections.singletonList(apiKeyAuthorization()))//adds the authorization button 
						.securityContexts(Collections.singletonList(securityContext()));//change to Arrays.asList for more than 1 param
			}
			
			@SuppressWarnings("deprecation")//what is the latest Type used? 
			private ApiInfo getApiInfo() {
						return new ApiInfo("Fraud Eagle Eye Authorization Server", "This page lists all the APIs in Fraud-Eagle-Eye-Authorization-server",
						"1.0", "our terms are client friendly", "call Abel on 08067950474", "licence 1.0", "http://abellicence.com");
			}
			//added code from Stan..
			private ApiKey apiKeyAuthorization(){
		        return new ApiKey(AUTHORIZATION, AUTHORIZATION, "header");
		    }

		    private SecurityContext securityContext(){
		        return SecurityContext.builder()
		                .securityReferences(defaultAuth())
		                .build();
		    }

		    private List<SecurityReference> defaultAuth(){
		        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		        authorizationScopes[0] = authorizationScope;
		        return Collections.singletonList(new SecurityReference(AUTHORIZATION, authorizationScopes));
		    }
}
