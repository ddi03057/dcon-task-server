package kr.co.dcon.taskserver.common.exception;

public class UserNoGroupException extends KeyCloakRestException{
	private static final long serialVersionUID = 1L;
	
	public UserNoGroupException() {
        super();
    }

    public UserNoGroupException(String userId) {
       super(String.format("user: [%s] has no group", userId));
    }
}
