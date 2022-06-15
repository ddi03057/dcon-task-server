package kr.co.dcon.taskserver.common.exception;

public class KeyCloakRestException  extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public KeyCloakRestException() {
        super();
    }

    public KeyCloakRestException(String message) {
        super(message);
    }

}
