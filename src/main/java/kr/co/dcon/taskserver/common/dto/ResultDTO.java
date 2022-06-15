
package kr.co.dcon.taskserver.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(Include.NON_NULL)
public class ResultDTO {

	public static final String STATUS_OK = "ok";
	public static final String STATUS_FAIL = "fail";

	@JsonProperty("status")
	private String status = STATUS_OK;

	@JsonProperty(value = "error", required = false)
	private ErrorDTO error = null;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setError(String code, String message) {
		if(error == null) {
			error = new ErrorDTO();
		}

		status = STATUS_FAIL;
		error.setCode(code);
		error.setMessage(message);
	}

	public ErrorDTO getError() {
		return error;
	}
}
