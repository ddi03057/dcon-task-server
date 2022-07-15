package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserInfoDTO {

    @ApiModelProperty(value = "사용자 이름", notes = "사용자 이름", example = "홍길동")
    private String userName;

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "test1@dc-on.co.kr")
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @ApiModelProperty(value = "userId", notes = "userId", example = "3acd88f9-faf8-4827-9d54-3769343c9ef2")
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userId;

    private String createId;
    private String createDate;

    @ApiModelProperty(value = "token", notes = "token", example = "3acd88f9-faf8-4827-9d54-3769343c9ef2")
    private String token;

}
