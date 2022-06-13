package com.workmotion.common.exception;

/**
 * @author Adel
 * Represents an Exception that occurs in the normal flow of an application that needs to be caught and handled
 */
public class EMPException extends Exception {

	private static final long serialVersionUID = 6309868230161991714L;

	private int statusCode;
	
	public EMPException(Throwable cause) {
        super(cause);
    }

    public EMPException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public EMPException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public void setStatusCode(int statusCode) {
    	this.statusCode = statusCode;
    }

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
}
