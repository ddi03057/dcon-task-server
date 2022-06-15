package kr.co.dcon.taskserver.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserCreateDTO {
	
	private String serviceCode;
	private String userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String registCompanyName;
	private String locale;
	private String userTelNoCtrCd;
	private String userTelNo;
	private Boolean receiveSMS;
	private String password;
//	private List<GroupDTO> groups;

}
