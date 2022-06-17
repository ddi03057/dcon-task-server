package kr.co.dcon.taskserver.user.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
	
	private String userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String locale;
	private String userTelNo;
	private String password;

//	private List<GroupDTO> groups;

}
