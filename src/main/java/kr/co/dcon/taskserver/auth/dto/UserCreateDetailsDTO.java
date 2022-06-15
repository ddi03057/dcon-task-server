package kr.co.dcon.taskserver.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserCreateDetailsDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "직원번호", notes = "직원번호", example = "10000423")
	private String employeeCode;

	@ApiModelProperty(value = "이름", notes = "이름", example = "박수석")
	private String employeeName;

	@ApiModelProperty(value = "이메일", notes = "이메일", example = "suseok.park@bespinglobal.com")
	private String employeeEmail;

	@ApiModelProperty(value = "연락처", notes = "연락처", example = "010-1234-5678")
	private String employeeTel;


	
}
