package kr.co.dcon.taskserver.common.constants;

import lombok.Getter;


@Getter
public enum ResultCode {
	//	success
	OK(0, "OK"),

	//	general error code
	BAD_REQUEST(9400, "badRequest"),
	NO_AUTHRORIZATION(9401, "noAuthrorization"),
	ACCESS_DENY(9403, "accessDeny"),
	NOT_FOUND(9404, "notFound"),
	SERVER_ERROR(9500, "internalServerError"),
	EXIST(4000, "EXIST ERROR"),
	FILE_CHUNK_EXCEPPTION(8000, "file chunk not found exception"),
	FILE_NOT_EXISTS_EXCEPTION(8100, "file not exisist exception"),
	FILE_STORAGE_EXCEPTION(8200, "FileStorageException"),
	FILE_NAME_EXCEPTION(8300, "FileNameException"),
	BU_DIVISON_NOT_EXISTS_EXCEPTION(8400, "No BU , DIVISIONAL, TEAM authority"),
	USER_EXISTS_EXCEPTION(8500, "User exists"),
	USER_NOT_EXISTS_EXCEPTION(8600, "User not exists"),
	SEND_MAIL_FAIL(1000, "Send mail Fail"),
	NO_USER(9406, "NO USER"),
	ETC_ERROR(9999, "etcError");

	private final int code;
	private final String description;

	ResultCode(int code, String description) {
		this.code = code;
		this.description = description;
	}
}
