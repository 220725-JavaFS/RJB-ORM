package com.Matrix.Exception;

public class JsonMapperException extends RuntimeException {
	public JsonMapperException() {
		
	}
	
	public JsonMapperException(String Message) {
		super(Message);
	}
	
	public JsonMapperException(String Message, Exception e) {
		super(Message, e);
	}
}
