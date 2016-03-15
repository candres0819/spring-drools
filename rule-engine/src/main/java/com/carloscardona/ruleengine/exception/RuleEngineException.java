package com.carloscardona.ruleengine.exception;

/**
 * 
 * @author carlos.cardona
 * 
 */
public class RuleEngineException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public RuleEngineException(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public RuleEngineException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param cause
	 */
	public RuleEngineException(final Throwable cause) {
		super(cause);
	}
}