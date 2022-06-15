package kr.co.dcon.taskserver.auth.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontProfileDTO {

	private String env;

	private String url;

	private String realm;

	
}
