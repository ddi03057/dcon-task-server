package kr.co.dcon.taskserver.sample.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.dcon.taskserver.common.constants.ResultCode;
import kr.co.dcon.taskserver.common.dto.ResponseDTO;
import kr.co.dcon.taskserver.common.dto.ResultDTO;
import kr.co.dcon.taskserver.sample.dto.SampleDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListDTO;
import kr.co.dcon.taskserver.sample.dto.SampleListReqDTO;
import kr.co.dcon.taskserver.sample.dto.SampleReqDTO;
import kr.co.dcon.taskserver.sample.service.SampleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(value = "SAMPLE API")
@RequestMapping(value = "/api/v1/sample")
@RestController
@AllArgsConstructor
public class SampleController {
   // @Autowired
    SampleService sampleService;

    @ApiOperation(value = "sample list", notes = " sample list")
    @GetMapping("/list")
    public ResponseDTO<SampleListDTO> selectSampleList(@Valid SampleListReqDTO reqDTO) {
        log.info("rereqDTOq::{}",reqDTO.toString());
       // return new ResponseDTO<>(ResultCode.OK, sampleService.selectSampleList(reqDTO));
        return sampleService.selectSampleList(reqDTO);

    }
/*
    @ApiOperation(value = "sample detail", notes = " sample detail")
    @GetMapping("/detail")
    public ResponseDTO<SampleDTO> selectSampleDetail(@Valid SampleListReqDTO reqDTO) {
        return new ResponseDTO<>(ResultCode.OK, sampleService.selectSampleDetail(reqDTO));

    }*/
/*
    @ApiOperation(value = "sample insert", notes = " sample insert")
    @PostMapping("/insert")
    public ResponseDTO<ResultDTO> insert (@Valid @RequestBody SampleReqDTO reqDTO) {
        try {
            sampleService.sampleInsert(reqDTO);
        } catch (Exception e) {
            new ResponseDTO<>(ResultCode.ETC_ERROR);
        }
        return new ResponseDTO<>(ResultCode.OK);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "sample 수정", notes = "sample 수정")
    public ResponseDTO<SampleDTO> updateSample(@Valid @RequestBody SampleReqDTO reqDTO,
                                               @ApiParam(value = " 아이디", required = true, example = "0") @PathVariable String id){
        reqDTO.setSeq(id);
        int updateCnt = sampleService.sampleUpdate(reqDTO);

        if(updateCnt < 0) {
            new ResponseDTO<>(ResultCode.ETC_ERROR);
        }
        return new ResponseDTO<>(ResultCode.OK);
    }

    @ApiOperation(value = "sample 삭제")
    @DeleteMapping("{id}")
    public ResponseDTO<Boolean> deleteSample(@ApiParam(value = "번호", required = true) @PathVariable String id){

        try {
            sampleService.deleteSample(id);
        } catch (Exception e) {
            new ResponseDTO<>(ResultCode.ETC_ERROR);
        }
        return new ResponseDTO<>(ResultCode.OK);
    }
    */
}
