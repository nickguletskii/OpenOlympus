package org.ng200.openolympus.exceptions;

public class SecurityAuditCatastrophicFailureException
		extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2752017105047657359L;

	public SecurityAuditCatastrophicFailureException() {
	}

	public SecurityAuditCatastrophicFailureException(String message) {
		super(message);
	}

	public SecurityAuditCatastrophicFailureException(Throwable cause) {
		super(cause);
	}

	public SecurityAuditCatastrophicFailureException(String message,
			Throwable cause) {
		super(message, cause);
	}

	public SecurityAuditCatastrophicFailureException(String message,
			Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
