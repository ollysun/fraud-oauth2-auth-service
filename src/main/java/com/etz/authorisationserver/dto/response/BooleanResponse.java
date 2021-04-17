package com.etz.authorisationserver.dto.response;

import com.etz.authorisationserver.util.RequestUtil;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BooleanResponse{


	private HttpStatus status;
    private String message;
	private double execTime; 
	private String error;

    private Boolean data;

    public BooleanResponse(Object data) {
    	setStatus(HttpStatus.OK);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData((Boolean) data);
    }
}