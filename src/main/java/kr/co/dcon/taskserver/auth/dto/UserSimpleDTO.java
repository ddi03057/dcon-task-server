package kr.co.dcon.taskserver.auth.dto;

import lombok.Data;


@Data
public class UserSimpleDTO{
	@SuppressWarnings("unused")
	private String userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String userEmail;
	private String auth;
	private String loginType;
	private String useYn;
	private String locale;
	private String passwordInitYn;



}
