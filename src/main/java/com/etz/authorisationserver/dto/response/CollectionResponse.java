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


	private Integer status;
    private String message;
	private double execTime; 
	private String error;

    private Collection<T> data;

    public CollectionResponse(Collection<T> result) {
        setData(result);
        setStatus(HttpStatus.OK.value());
        //setMessage(RequestUtil.getMessage());
        //setExecTime((System.nanoTime() - RequestUtil.getStartTime())/1000000);
    }
}