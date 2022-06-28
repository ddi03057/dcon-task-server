package kr.co.dcon.taskserver.code.controoler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.dcon.taskserver.code.dto.CodeListDTO;
import kr.co.dcon.taskserver.code.dto.CodeReqDTO;
import kr.co.dcon.taskserver.code.service.CodeService;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Api(value = "code API")
@RequestMapping(value = "/api/v1/code")
@RestController
public class CodeController {

    CodeService codeService;

    public CodeController(CodeService codeService){
        this.codeService = codeService;
    }

    @ApiOperation(value = "codeList", notes = "codeList")
    @GetMapping("/codeList")
    public ResponseDTO<List<CodeListDTO>> selectCodeList(@Valid CodeReqDTO reqDTO){
        return  codeService.selectCodeList(reqDTO);
    }
}
