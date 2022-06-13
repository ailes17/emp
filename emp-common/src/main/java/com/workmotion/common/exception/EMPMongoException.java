package com.workmotion.common.exception;

/**
 * 
 * @author Adel
 * Represents an Exception that occurs at MongoDB level and which needs to get handled at upper level
 *
 */
public class EMPMongoException extends Exception {

	private static final long serialVersionUID = 7493140934268006430L;
	
	public EMPMongoException(Throwable cause) {
        super(cause);
    }
	
}
