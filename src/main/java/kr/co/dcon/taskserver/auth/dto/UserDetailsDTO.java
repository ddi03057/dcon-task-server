package kr.co.dcon.taskserver.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UserDetailsDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userId;
	@ApiModelProperty(value = "이름", notes = "이름", example = "김지희", required = true)
	private String userName;
	private String firstName;
	private String lastName;
	
	@ApiModelProperty(value = "이메일", notes = "이메일", example = "jihee.kim@bespinglobal.com", required = true)
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String userEmail;
	private int errorCnt;
	@ApiModelProperty(value = "연락처", notes = "연락처", example = "010-1234-5678", required = true)
	@Pattern(regexp = "^(?:\\d\\s?){9,20}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	private String userTelNo;
	private String userTelNoCtrCd;
	private String useYn;
	@ApiModelProperty(value = "회사코드", notes = "회사코드", example = "1000", required = true)
	private String companyCode;
	@ApiModelProperty(value = "직원 고객번호", notes = "직원 고객번호", example = "X10000", required = true)
	private String customerCode;
	@ApiModelProperty(value = "직원 번호", notes = "직원 번호", example = "1311301010", required = true)
	private String customerDeptCode;
	
	private String lastAccessTime;
	
	private String createdDate;
	private String creatorId;
	private String updated;
	private String updaterId;

	@ApiModelProperty(value = "권한", notes = "권한", example = "BS", required = true)
	private String auth;
	private String loginType;
	private String email;

	@ApiModelProperty(value = "회사", notes = "회사", example = "베스핀글로벌", required = true)
	private String customerName;
	private String customerDeptName;
	private String customerRegistorNo;
	private String pos;
	private String locD;
	private String city;
	private String searchAuth;
	private String authName;
	private String useYnName;
	private String billKey;
	private String cardStatus;
	private String payMhCd;
	@ApiModelProperty(value = "사번", notes = "사번", example = "10000708", required = true)
	private String empNo; // 10000708

	@ApiModelProperty(value = "조회 권한", notes = "조회 권한", example = "BU")
	private String leader;

	@ApiModelProperty(value = "조회 권한 부서", notes = "조회 권한 부서", example = "BU")
	private String leaderDeptCode;

	private String jobTitleCode;
	private String empAuthDept;
	private String duty;
	private String employeeCode;
	private String deptTeamCode;
	
	//private GroupDTO group;
	private String siteCode;
	private String locale;
}

