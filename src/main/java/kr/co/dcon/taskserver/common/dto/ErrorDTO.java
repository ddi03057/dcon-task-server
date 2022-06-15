package kr.co.dcon.taskserver.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorDTO {

	@JsonProperty("code")
	private String code = "";

	@JsonProperty("message")
	private String message = "";
}
