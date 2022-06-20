package kr.co.dcon.taskserver.common.constants;

import lombok.Getter;
@Getter
public enum ResultCode {
	//	success
	OK(0, "OK", "성공"),

	//	Token Error
	INVALID_TOKEN(1000, "invalid tokey type", "잘못된 토큰 타입"),
	INVALID_TOKEN_NOT_FOUND_REALM(1001, "The name of realm does not exist in token.", "토큰에 램 이름이 없습니다."),
	INVALID_TOKEN_NOT_FOUND_EMAIL(1002, "The email address does not exist in token.", "토큰에 이메일이 없습니다."),

	// queue, message
	JMS_ERROR(2001, "JMS error", "JMS 에러"),
	JSON_PARSE_ERROR(2002, "json parse error", "JMS 파싱에러"),

	FILE_CHUNK_EXCEPTION(8000, "file chunk not found exception", "파일 청크 에러"),
	FILE_NOT_EXISTS_EXCEPTION(8100, "file not exisist exception", "파일 존재하지 않음"),
	FILE_STORAGE_EXCEPTION(8200, "FileStorageException", "파일 스토리지 예외"),
	FILE_NAME_EXCEPTION(8300, "FileNameException", "파일이름 에러"),
	//	general error code
	BAD_REQUEST(9400, "badRequest", "잘못된 요청"),
	NO_AUTHRORIZATION(9401, "noAuthrorization", "권한 없음"),
	ACCESS_DENY(9403, "accessDeny", "접근 거부"),
	NOT_FOUND(9404, "notFound", "찾을 수 없음"),
	USER_EXIST_ALREADY(9405, "existAlready", "해당회원이 이미 있습니다"),
	NOT_EMPTY(9406, "not empty", "비어있지 않음"),

	SERVER_ERROR(9500, "internalServerError", "내부 서버 오류"),
	INSUFFICIENT_PARAMETER(9601, "insufficient parameter", "인자 부족"),
	INVALID_PARAMETER(9602, "invalid parameter", "바르지 못한 인자"),


	SEND_MAIL_FAIL(1000, "Send mail Fail", "메일 보내기 실패"),

	USER_EXISTS_EXCEPTION(8500, "User exists", "사용자가 있습니다"),
	USER_NOT_EXISTS_EXCEPTION(8600, "User not exists","사용자가 없습니다."),
	ETC_ERROR(9999, "etcError", "기타 에러") ;



	private final int code;
	private final String description;
	private  final  String descriptionKr;

	ResultCode(int code, String description, String descriptionKr) {
		this.code = code;
		this.description = description;
		this.descriptionKr = descriptionKr;
	}
}
