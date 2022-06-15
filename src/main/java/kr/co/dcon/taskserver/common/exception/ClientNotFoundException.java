package kr.co.dcon.taskserver.common.exception;

public class ClientNotFoundException extends KeyCloakRestException{
	private static final long serialVersionUID = 1L;

	public ClientNotFoundException() {
        super();
    }

    public ClientNotFoundException(String message) {
       super(message);
    }
}
