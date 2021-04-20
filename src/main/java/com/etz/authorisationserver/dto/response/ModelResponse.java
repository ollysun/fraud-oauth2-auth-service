package com.etz.authorisationserver.dto.response;

import com.etz.authorisationserver.util.RequestUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModelResponse<T> {


	private HttpStatus status;
    private String message;
	private double execTime; 
	private String error;

    private T data;

    public ModelResponse(T data) {
    	setStatus(HttpStatus.OK);
    	//setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        //setMessage(RequestUtil.getMessage());
        setData(data);
    }
}