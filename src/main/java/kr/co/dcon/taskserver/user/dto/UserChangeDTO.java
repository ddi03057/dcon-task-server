package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserChangeDTO {

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "ripple31222@naver.com", required = true)
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "userLocale", notes = "userLocale", example = "ko", required = true)
    private String userLocale;

    @ApiModelProperty(value = "userAuth", notes = "userAuth", example = "admin", required = true)
    private String userAuth;
    private String useYn;
}


