package kr.co.dcon.taskserver.common.controller;


import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(RuntimeExceptionBase.class)
	public ResponseDTO<String> handleRuntimeExceptionBase(RuntimeExceptionBase exception) {
		log.error("unhandled control error(runtime)", exception);
		return new ResponseDTO<>(exception.getErrorCode(), exception.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseDTO<String> handleException(Exception exception) {
		log.error("unhandled control error", exception);
		return new ResponseDTO<>(ResultCode.SERVER_ERROR, exception.getMessage());
	}

}
