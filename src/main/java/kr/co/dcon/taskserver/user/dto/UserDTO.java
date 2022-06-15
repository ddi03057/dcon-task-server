package kr.co.dcon.taskserver.user.dto;

import io.swagger.annotations.ApiModelProperty;
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
	
	@ApiModelProperty(value = "이메일", notes = "이메일", example = "jihee.kim@bespinglobal.com", required = true)
	public String getUserEmail() {
		return this.token.getEmail();
	}

	//public String getAuth() {
	//	return "PA";
	//}
	

	@ApiModelProperty(value = "이름", notes = "이름", example = "김지희", required = true)
	public String getUserName() {
		return String.valueOf(this.token.getOtherClaims().get("USER_NM")); 
	}
	
}
