package kr.co.dcon.taskserver.testYh.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
public class TestYhDetailReqDTO {
    @ApiModelProperty(value = "회사코드",required = true)
    @NotBlank
    @Size
    private String companyCode;

    @ApiModelProperty(value = "사용자 이름", notes = "이름", example = "홍길동", required = true)
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "test@dc-on.co.kr", required = true)
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "검색구분", notes = "검색구분", example = "email", required = true)
    @NotBlank
    private String searchGubun;
}
