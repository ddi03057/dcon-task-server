package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfoReqDTO {

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "suseokpark@dc-on.co.kr")
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "pw", notes = "pw", example = "1233")
    private String userPwd;

}
