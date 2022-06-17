package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.dcon.taskserver.common.constants.UserOtherClaim;
import lombok.ToString;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

@ToString
public class UserDTO {
	private KeycloakSecurityContext  context;
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

	//public String getAuth() {
	//	return "PA";
	//}
	

	@ApiModelProperty(value = "이름", notes = "이름", example = "테스트", required = true)
	public String getUserName() {
		return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.USER_NAME));
	}
	public String getUserAuth() {
		return String.valueOf(this.token.getOtherClaims().get(UserOtherClaim.AUTH));
	}

}
