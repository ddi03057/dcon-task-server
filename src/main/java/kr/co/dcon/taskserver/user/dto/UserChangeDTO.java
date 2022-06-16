package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserChangeDTO {

	@ApiModelProperty(value = "비밀번호", notes = "비밀번호", example = "abc123")
	private String password;

	@ApiModelProperty(value = "이메일", notes = "이메일", example = "ripple31222@naver.com", required = true)
	@NotBlank
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String userEmail;

//	@ApiModelProperty(value = "전화번호")
//	@Pattern(regexp = "^(?:\\d\\s?){9,20}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
//	private String telNo;
//

	@ApiModelProperty(value = "result")
	private String result;

	@ApiModelProperty(value = "사용자 ID")
	private String userId;
	
	@ApiModelProperty(value = "firstName", notes = "firstName", example = "박", required = true)
	private String firstName;
	
	@ApiModelProperty(value = "lastName", notes = "lastName", example = "테스트", required = true)
	private String lastName;
	
	private String locale;
}


