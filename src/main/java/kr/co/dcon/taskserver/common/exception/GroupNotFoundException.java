package kr.co.dcon.taskserver.common.exception;

public class GroupNotFoundException  extends KeyCloakRestException{

	private static final long serialVersionUID = 1L;
	
	public GroupNotFoundException() {
        super();
    }

    public GroupNotFoundException(String path) {
       super("group not found  with path:"  + path);
    }

}
