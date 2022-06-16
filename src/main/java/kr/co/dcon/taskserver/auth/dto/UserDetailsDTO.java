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
	@ApiModelProperty(value = "이름", notes = "이름", example = "박수석", required = true)
	private String userName;
	private String firstName;
	private String lastName;
	
	@ApiModelProperty(value = "이메일", notes = "이메일", example = "suseokpark@dc-on.co.kr", required = true)
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String userEmail;
	private int errorCnt;
	@ApiModelProperty(value = "연락처", notes = "연락처", example = "010-1234-5678", required = true)
	@Pattern(regexp = "^(?:\\d\\s?){9,20}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	private String userTelNo;
	private String useYn;

	private String lastAccessTime;
	
	private String createdDate;
	private String creatorId;
	private String updated;
	private String updaterId;

	@ApiModelProperty(value = "권한", notes = "권한", example = "BS", required = true)
	private String auth;
	private String loginType;
	private String email;

	private String searchAuth;
	private String authName;
	private String useYnName;

	private String locale;
}

