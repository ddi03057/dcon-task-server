package kr.co.dcon.taskserver.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImpersonateDTO {
	private String authServerUrl;
	private String token;
	private String realm;
	private String userId;
	private String destnationUrl;
}
