package kr.co.dcon.taskserver.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendEmailResultDTO extends ResultDTO {

	@JsonProperty("result")
	private String result;

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
}
