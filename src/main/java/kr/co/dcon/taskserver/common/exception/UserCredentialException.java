package kr.co.dcon.taskserver.common.exception;

public class UserCredentialException extends KeyCloakRestException{

private static final long serialVersionUID = 1L;
	
	public UserCredentialException() {
        super();
    }

    public UserCredentialException(String message) {
       super(message);
    }
}
