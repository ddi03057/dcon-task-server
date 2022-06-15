package kr.co.dcon.taskserver.common.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserInfoException   extends AuthenticationException{
	private static final long serialVersionUID = 1L;
	
	public InvalidUserInfoException(String msg) {
        super(msg);
    }
}
