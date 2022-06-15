package kr.co.dcon.taskserver.auth.dto;

import lombok.Data;


@Data
public class UserSimpleDTO{
	@SuppressWarnings("unused")
	private String userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String userNm;
	private String userEmail;
	private String customerCode;
	private String deptCode;
	private String auth;
	private String loginType;
	private String empNo;
	private String jobTitleCode; // TEAM(2개자르고 like검색), DIV(끝에 3개자르고 like 검색, BU(끝에 04개 자르고 like 검색)
	private String empAuthDept;
	private String useYn;

	//ocp에서  사용자 이메일이 바로 아이디임 
	// 따로 아이디 관리 안함.
	public String getUserId() {
		return this.userEmail;
	}
	

}
