package kr.co.dcon.taskserver.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.dcon.taskserver.auth.dto.FrontProfileDTO;
import kr.co.dcon.taskserver.auth.service.FrontProfileService;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.util.DomainUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
@Api(value = "front end profile")
public class FrontProfileController {

	@Value("${front.env}")
	private String env;

	FrontProfileService service;

	public FrontProfileController(FrontProfileService service){
		this.service = service;
	}
	@GetMapping("/front-profile")
	@ApiOperation(value = "front end profile 조회")
	public ResponseDTO<FrontProfileDTO> getFrontProfile(HttpServletRequest request){
		String rootUrl = DomainUtil.getRootUrl(request.getRequestURL().toString());
		return new ResponseDTO<>(ResultCode.OK, service.selectFrontFile(rootUrl));
	}
}
