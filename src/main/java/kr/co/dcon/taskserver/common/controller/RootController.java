package kr.co.dcon.taskserver.common.controller;

import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.exception.RuntimeExceptionBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RootController{

	@GetMapping("/")
	public ResponseDTO<String> getHome() {
		return new ResponseDTO<>(ResultCode.OK, "ok root");
	}

	@GetMapping("/health")
	public ResponseDTO<String> checkHealth() { //readiness&Liveness
		return new ResponseDTO<>(ResultCode.OK, "ok health");
	}

	@GetMapping("/error-sample")
	public ResponseDTO<String> getSampleError() { //error sample
		throw new RuntimeExceptionBase(ResultCode.SERVER_ERROR);
	}



}
