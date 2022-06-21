package kr.co.dcon.taskserver.sample.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.sample.dto.SampleDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListReqDTO;
import kr.co.dcon.taskserver.sample.dto.SampleReqDTO;
import kr.co.dcon.taskserver.sample.service.SampleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "SAMPLE API")
@RequestMapping(value = "/api/v1/sample")
@RestController
public class SampleController {
    SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @ApiOperation(value = "sample list", notes = " sample list")
    @GetMapping("/list")
    public ResponseDTO<SampleListDTO> selectSampleList(@Valid SampleListReqDTO reqDTO) {
        return sampleService.selectSampleList(reqDTO);

    }

    @ApiOperation(value = "sample detail", notes = " sample detail")
    @GetMapping("/detail")
    public ResponseDTO<SampleDTO> selectSampleDetail(@Valid SampleListReqDTO reqDTO) {
        return sampleService.selectSampleDetail(reqDTO);

    }

    @ApiOperation(value = "sample insert", notes = " sample insert")
    @PostMapping("/insert")
    public ResponseDTO<Map<String, String>> insert(@Valid @RequestBody SampleReqDTO reqDTO) {

        Map<String, String> result = new HashMap<>();
        String insertResult = "";
        boolean insert = sampleService.sampleInsert(reqDTO);

        if (insert) {
            insertResult = "Y";
        } else {
            insertResult = "N";
        }
        result.put("result", insertResult);
        return new ResponseDTO<>(ResultCode.OK, result);


    }

    @ApiOperation(value = "사용자 수정")
    @PutMapping("{userEmail}")
    public ResponseDTO<Map<String, String>> updateSample(@Valid @RequestBody SampleReqDTO reqDTO,
                                                         @ApiParam(value = "이메일", required = true, example = "dcon@dcon.co.kr") @PathVariable String userEmail) {
        Map<String, String> result = new HashMap<>();

        reqDTO.setUserEmail(userEmail);
        String updateResult = "";
        boolean insert = sampleService.sampleUpdate(reqDTO);
        if (insert) {
            updateResult = "Y";
        } else {
            updateResult = "N";
        }
        result.put("result", updateResult);
        return new ResponseDTO<>(ResultCode.OK, result);
    }

    @ApiOperation(value = "sample 삭제")
    @DeleteMapping("/sample/{userEmail}")
    public ResponseDTO<ResponseDTO<Map<String, String>>> deleteSample(@ApiParam(value = "userEmail", required = true) @PathVariable String userEmail) {

        sampleService.deleteSample(userEmail).getResultData();
        return new ResponseDTO<>(ResultCode.OK);

    }

}
