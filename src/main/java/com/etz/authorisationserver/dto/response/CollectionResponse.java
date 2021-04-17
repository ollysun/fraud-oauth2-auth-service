package com.etz.authorisationserver.dto.response;

import com.etz.authorisationserver.util.RequestUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Getter
@Setter
@ToString
public class CollectionResponse<T> {


	private HttpStatus status;
    private String message;
	private double execTime; 
	private String error;

    private Collection<T> data;

    public CollectionResponse(Collection<T> result) {
    	setStatus(HttpStatus.OK);
        setExecTime((System.nanoTime() - RequestUtil.getStartTime()) / 100000000);
        setMessage(RequestUtil.getMessage());
        setData(result);
    }
}