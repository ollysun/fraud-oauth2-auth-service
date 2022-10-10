package com.etz.authorisationserver.dto.response;

import java.util.List;

public class RequestResetResponse {
	 private  boolean status;
	  private  String message;
	  private  List<?> data;
	  

 public RequestResetResponse(boolean b,String string,List<?> list) {
		// TODO Auto-generated constructor stub
	    this.status = b;
	    this.message = string;
	     this.data = list;
	     
 }
}