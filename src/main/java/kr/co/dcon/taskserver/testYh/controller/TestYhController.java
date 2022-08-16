package kr.co.dcon.taskserver.testYh.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhListDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhListReqDTO;
import kr.co.dcon.taskserver.testYh.dto.TestYhReqDTO;
import kr.co.dcon.taskserver.testYh.service.TestYhService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Api(value = "TESTYH API")
@RequestMapping(value = "/api/v1/testYh")
@RestController
public class TestYhController {

    TestYhService testYhService;

    /*(DI) 생성자 주입으로 의존성 주입 서비스객체를 컨트롤객체에 주입하여
    프레임워크가 객체생성을 유도할 수 있도록*/
    public TestYhController(TestYhService testYhService){
        this.testYhService = testYhService;
    }

    @ApiOperation(value = "testYh list", notes = "testYh list")
    @GetMapping("/list")
    public ResponseDTO<TestYhListDTO> selectTestYhList(@Valid TestYhListReqDTO reqDTO){
        return testYhService.selectTestYhList(reqDTO);
    }

    @ApiOperation(value = "testYh detail", notes ="testYh detail")
    @GetMapping("/detail")
    public ResponseDTO<TestYhDTO> selectTestYhDetail(@Valid TestYhListReqDTO reqDTO){
        return testYhService.selectTestYhDetail(reqDTO);
    }

    @ApiOperation(value = "testYh insert", notes = "testYh insert")
    @PostMapping("/insert")
    public ResponseDTO<Map<String, String>> insert(@Valid @RequestBody TestYhReqDTO reqDTO){
        Map<String, String> result = new HashMap<>();
        String insertResult = "";
        boolean insert = testYhService.testYhInsert(reqDTO);
        if (insert){
            insertResult ="Y";
        }else{
            insertResult ="N";
        }
        result.put("result",insertResult);
        return new ResponseDTO<>(ResultCode.OK,result);
    }
    @ApiOperation(value = "사용자 수정")
    @PutMapping("{ueserEmail}")
    public ResponseDTO<Map<String, String>> updateTestYh(@Valid @RequestBody TestYhReqDTO reqDTO,
                                                         @ApiParam(value = "이메일", required = true, example = "dcon@dcon.co.kr")@PathVariable String userEmail){
        Map<String, String> result = new HashMap<>();

        reqDTO.setUserEmail(userEmail);
        String updateResult = "";
        boolean insert = testYhService.testYhUpdate(reqDTO);
        if(insert){
            updateResult ="Y";
        }
        else{
            updateResult = "N";
        }
        result.put("result",updateResult);
        return new ResponseDTO<>(ResultCode.OK, result);
    }

    @ApiOperation(value = "testYh 삭제")
    @DeleteMapping("testYh/{userEmail}")
    public ResponseDTO<ResponseDTO<Map<String, String>>> deleteTestYh(@ApiParam(value = "userEmail",required = true) @PathVariable String userEmail){
        testYhService.deleteTestYh(userEmail).getResultData();
        return new ResponseDTO<>(ResultCode.OK);
    }

}
