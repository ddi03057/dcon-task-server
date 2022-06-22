package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserChangePasswordDTO {
    @ApiModelProperty(value = "사용자ID", example = "dcon", required = false)
    private String userId;
    @ApiModelProperty(value = "사용자 EMAIL", example = "dcon@dc-on.co.kr", required = false)
    private String email;
    @ApiModelProperty(value = "사용자 비밀번호", example = "test1234", required = false)
    private String newCredential;
    @ApiModelProperty(value = "사용자 비밀번호 확인", example = "test1234", required = false)
    private String newCredentialConfirm;
    @ApiModelProperty(value = "초기 비밀번호 확인", example = "Y", required = false)
    private String initedPasswordYn;
}
