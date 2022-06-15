package kr.co.dcon.taskserver.common.exception;

public class GroupAttributeExceptioin  extends KeyCloakRestException{

	private static final long serialVersionUID = 1L;

	public GroupAttributeExceptioin() {
        super();
    }

    public GroupAttributeExceptioin(String message) {
       super(message);
    }

}
