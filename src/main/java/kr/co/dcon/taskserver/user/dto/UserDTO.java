package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import lombok.ToString;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

@ToString
public class UserDTO {
    private KeycloakSecurityContext context;
    private AccessToken token;

    public UserDTO(KeycloakSecurityContext context) {
        this.context = context;
        this.token = context.getToken();
    }

    public String getUserId() {
        return this.token.getSubject();
    }

    @ApiModelProperty(value = "이메일", notes = "이메일", example = "suseokpark@dc-on.co.kr", required = true)
    public String getUserEmail() {
        return this.token.getEmail();
    }

    @ApiModelProperty(value = "이름", notes = "이름", example = "테스트", required = true)
    public String getUserName() {
        return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.USER_NAME));
    }

    @ApiModelProperty(value = "getUserAuth", notes = "getUserAuth", example = "admin", required = true)
    public String getUserAuth() {
        return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.AUTH));
    }

    @ApiModelProperty(value = "useYn", notes = "useYn", example = "Y", required = true)
    public String getUseYn() {
        return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.USE_YN));
    }

    public String getPasswordInitYn() { return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.PASSWORD_INIT_YN));}
    @ApiModelProperty(value = "userLocale", notes = "userLocale", example = "ko")
    public String getUserLocale() { return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.USER_LOCALE));}
}
