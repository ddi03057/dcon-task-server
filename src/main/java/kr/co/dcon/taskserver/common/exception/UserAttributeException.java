package kr.co.dcon.taskserver.common.exception;

public class UserAttributeException extends KeyCloakRestException {

	private static final long serialVersionUID = 1L;

	public UserAttributeException() {
        super();
    }

    public UserAttributeException(String userId) {
       super(String.format("user: [%s] has no attribute", userId));
    }


}
